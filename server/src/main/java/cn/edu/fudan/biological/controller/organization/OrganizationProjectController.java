package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.controller.request.user.UserProjectRequest;
import cn.edu.fudan.biological.domain.*;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.*;
import cn.edu.fudan.biological.util.DateUtil;
import cn.edu.fudan.biological.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * @program: biological
 * @description: 单位项目相关功能
 * @author: Shen Zhengyu
 * @create: 2021-03-14 18:07
 **/
@Slf4j
@RestController
@RequestMapping(value = "/api/unit")
public class OrganizationProjectController {

  private final OrganizationInfoRepository organizationInfoRepository;

  private final ProjectInfoRepository projectInfoRepository;

  private final ProjectDataRepository projectDataRepository;

  private final AgreementInfoRepository agreementInfoRepository;

  private final AgreementResponseRepository agreementResponseRepository;

  @Autowired
  public OrganizationProjectController(OrganizationInfoRepository organizationInfoRepository,
      ProjectInfoRepository projectInfoRepository, ProjectDataRepository projectDataRepository,
      AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository) {
    this.organizationInfoRepository = organizationInfoRepository;
    this.projectInfoRepository = projectInfoRepository;
    this.projectDataRepository = projectDataRepository;
    this.agreementInfoRepository = agreementInfoRepository;
    this.agreementResponseRepository = agreementResponseRepository;
  }

  @PostMapping("/project")
  public MyResponse createProject(@RequestParam String organization, @RequestParam String projectName,
      @RequestParam String ReleaseTime, @RequestParam String projectGoal, @RequestParam String projectDuration,
      @RequestParam(name = "data", required = false) List<String> data, HttpServletResponse response,
      HttpServletRequest request) {
    Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
    if (organization_info == null) {
      return MyResponse.fail("用户名不存在", 1102);
    }
    String[] times = projectDuration.split("-");
    Date startDate = DateUtil.StringToDate(times[0]);
    Date endDate = DateUtil.StringToDate(times[1]);
    Date createDate = DateUtil.StringToDate(ReleaseTime);
    Project_info newProject = null;
    newProject = new Project_info(startDate, endDate, projectGoal, organization_info);
    newProject.setCreateTime(createDate);
    newProject.setName(projectName);
    Set<Project_data> all_project_data = new HashSet<>();
    for (String field : data) {
      Project_data project_data = new Project_data(newProject, field);
      all_project_data.add(project_data);
      projectDataRepository.save(project_data);
    }
    newProject.setData(all_project_data);
    projectInfoRepository.save(newProject);
    log.info(organization + "创建项目:" + newProject.toString());
    log.info("收集字段:" + data.toString());
    return MyResponse.success();
  }

  @PostMapping("/projectResult")
  public MyResponse reviewProjectResult(@RequestParam String projectId){
    Set<Agreement_info> agreement_infos = agreementInfoRepository.findAllByPid(Integer.parseInt(projectId));
    Set<HashMap<String,String>> data = new HashSet<>();
    if(agreement_infos == null){
      return MyResponse.fail("所操作的数据不存在", 1002);
    }else{
      for (Agreement_info agreement_info : agreement_infos) {
        HashMap<String,String> datum = new HashMap<>();
        datum.put("username",agreement_info.getUsername());
        for (Agreement_response respons : agreement_info.getResponses()) {
          datum.put(respons.getProjectData().getData(),respons.getResponse());
        }
        data.add(datum);
      }
      return MyResponse.success("成功",data);
    }
  }

  @PutMapping("/projectDetail")
  public MyResponse changeProject(@RequestParam String organization, @RequestParam Integer pid,
      @RequestParam String projectName, @RequestParam String ReleaseTime, @RequestParam String projectGoal, @RequestParam String projectDuration,
      @RequestParam(name = "data", required = false) List<String> data, HttpServletResponse response,
      HttpServletRequest request) {
    Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
    if (organization_info == null) {
      return MyResponse.fail("用户名不存在", 1102);
    }
    String[] times = projectDuration.split("-");
    Date startDate = DateUtil.StringToDate(times[0]);
    Date endDate = DateUtil.StringToDate(times[1]);
    Date createDate = DateUtil.StringToDate(ReleaseTime);
    Project_info newProject = null;
    newProject = projectInfoRepository.findByPid(pid);
    newProject.setCreateTime(createDate);
    newProject.setName(projectName);
    newProject.setStartTime(startDate);
    newProject.setEndTime(endDate);
    newProject.setPurpose(projectGoal);
    Set<Project_data> all_project_data = newProject.getData();
    all_project_data.clear();
    projectDataRepository.deleteAllByPid(pid);
    for (String field : data) {
      Project_data project_data = new Project_data(newProject, field);
      all_project_data.add(project_data);
      projectDataRepository.save(project_data);
    }
    newProject.setData(all_project_data);
    projectInfoRepository.save(newProject);
    log.info(organization + "更改项目:" + newProject.toString());
    log.info("收集字段:" + data.toString());
    return MyResponse.success();
  }

  @PostMapping("/projectDraft")
  public MyResponse savedProject(@RequestParam String organization, @RequestParam Integer pid,
      @RequestParam String projectName, @RequestParam String ReleaseTime, @RequestParam String projectGoal, @RequestParam String projectDuration,
      @RequestParam(name = "data", required = false) List<String> data, HttpServletResponse response,
      HttpServletRequest request) {
    Project_info content = projectInfoRepository.findByPid(pid);
    if(null == content){
      return MyResponse.fail("所操作的数据不存在", 1002);
    }else{
      createProject(organization, projectName, ReleaseTime, projectGoal, projectDuration, data, response, request);
      return MyResponse.success("成功", content);
    }
    }


  @PostMapping("/projects")
  public MyResponse projects(@RequestParam String organization, HttpServletResponse response,
      HttpServletRequest request) {
    Set<Project_info> project_infos = projectInfoRepository.findAllByOrganization(organization);
    HashSet<HashMap<String,Object>> finishedList = new HashSet<HashMap<String,Object>>();
    HashSet<HashMap<String,Object>> ongoingList = new HashSet<HashMap<String,Object>>();
    HashSet<HashMap<String,Object>> draftList = new HashSet<HashMap<String,Object>>();
    HashSet<HashMap<String,Object>>[] data = new HashSet[3];
    for (Project_info project_info : project_infos) {
      if(project_info.getStatus().equals("draft")){
        draftList.add(convertData(project_info));
      }else if(project_info.getStatus().equals("ongoing")){
        ongoingList.add(convertData(project_info));
      }else if(project_info.getStatus().equals("finished")){
        finishedList.add(convertData(project_info));
      }else{
        break;
      }
    }
    data[0] = finishedList;
    data[1] = ongoingList;
    data[2] = draftList;
    return MyResponse.success("成功", data);
  }

  //    @PostMapping("/projects")
//    public MyResponse projects(@RequestParam String organization, @RequestParam String method, @RequestParam int number, HttpServletResponse response, HttpServletRequest request) {
//      List<Project_info> project_infos = projectInfoRepository.findAllByOrganizationAndStatusOrderByName(organization, method);
//      List<Project_info> content = new ArrayList<>();
//      int i = 1;
//      int nums = 0;
//      for (Project_info project_info : project_infos) {
//        if (i < number) {
//          break;
//        } else {
//          nums++;
//          content.add(project_info);
//          if (nums >= 10) {
//            break;
//          }
//        }
//        i++;
//      }
//      return MyResponse.success("成功", content);
//    }
  @PostMapping("/completedAgreements")
  public MyResponse completedAgreements(@RequestParam String organization, HttpServletResponse response, HttpServletRequest request) {
    Set<Project_info> project_infos = projectInfoRepository.findAllByOrganization(organization);
    Set<Agreement_response> data = new HashSet<>();
    for (Project_info project_info : project_infos) {
      int pid = project_info.getPid();
      Set<Agreement_info> agreement_infos = agreementInfoRepository.findAllByPid(pid);
      for (Agreement_info agreement_info : agreement_infos) {
        Set<Agreement_response> responses = agreement_info.getResponses();
        data.addAll(responses);
      }
    }
//    ArrayList<Agreement_response> datass = new ArrayList<>(datas);
//    datass.sort(new Comparator<Agreement_response>() {
//      @Override
//      public int compare(Agreement_response o1, Agreement_response o2) {
//        return o1.getAgreementId() - o2.getAgreementId();
//      }
//    });
//    ArrayList<Agreement_response> data = new ArrayList<>();

    return MyResponse.success("成功", data);
  }

  @PostMapping("/projectInfo")
  public MyResponse getProjectDetails(@RequestParam Integer projectId ) {
    Project_info projectInfo = projectInfoRepository.findByPid(projectId);
    if (projectInfo == null) {
      return MyResponse.fail("pid不存在", 1002);
    }
    return MyResponse.success("成功", convertData(projectInfo));
  }

  public static HashMap<String,Object> convertData(Project_info projectInfo){
    HashMap<String,Object> data = new HashMap<>();
    data.put("ProjectId",String.valueOf(projectInfo.getPid()));
    data.put("ProjectName",projectInfo.getName());
    data.put("ReleaseTime",projectInfo.getCreateTime());
    data.put("hot",String.valueOf(projectInfo.getHot()));
    data.put("ProjectGoal",projectInfo.getPurpose());
    data.put("organization",projectInfo.getOrganization());
    data.put("ProjectDuration",projectInfo.getStartTime()+ "-" + projectInfo.getEndTime());
    HashSet<String> fields = new HashSet<>();
    for (Project_data datum : projectInfo.getData()) {
      fields.add(datum.getData());
    }
    data.put("data",fields);
    return data;
  }
}
