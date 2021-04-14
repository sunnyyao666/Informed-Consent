package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.controller.request.unit.SaveProjectDraftRequest;
import cn.edu.fudan.biological.domain.*;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.*;
import cn.edu.fudan.biological.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder.In;
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

  private final ProjectItemRepository projectItemRepository;

  private final AgreementInfoRepository agreementInfoRepository;

  private final AgreementResponseRepository agreementResponseRepository;

  private final AgreementItemRepository agreementItemRepository;

  @Autowired
  public OrganizationProjectController(OrganizationInfoRepository organizationInfoRepository,
                                       ProjectInfoRepository projectInfoRepository, ProjectItemRepository projectItemRepository,
                                       AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository,AgreementItemRepository agreementItemRepository) {
    this.organizationInfoRepository = organizationInfoRepository;
    this.projectInfoRepository = projectInfoRepository;
    this.projectItemRepository = projectItemRepository;
    this.agreementInfoRepository = agreementInfoRepository;
    this.agreementResponseRepository = agreementResponseRepository;
    this.agreementItemRepository = agreementItemRepository;

  }



  @PostMapping("/projects")
  public MyResponse getAllProjectsOfUnit(@RequestParam String unitname){
    Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
    HashMap<Object,Object> data = new HashMap<>();
    HashSet<HashMap<Object,Object>> publishedList = new HashSet<>();
    HashSet<HashMap<Object,Object>> draftList = new HashSet<>();
    for (Project_info project : organization_info.getProjects()) {
      HashMap<Object,Object> tmp = new HashMap<>();
      tmp.put("projectId",project.getPid());
      tmp.put("projectName",project.getName());
      tmp.put("joinTime",project.getCreateTime());
      tmp.put("releaseTime",project.getReleaseTime());
      if (project.getStatus().equals("draft")){
        draftList.add(tmp);
      }else{
        publishedList.add(tmp);
      }
    }
    data.put("publishedList",publishedList);
    data.put("draftList",draftList);
    return MyResponse.success("成功",data);
  }
//  @PostMapping("/project")
//  public MyResponse createProject(@RequestParam String organization, @RequestParam String projectName,
//      @RequestParam String ReleaseTime, @RequestParam String projectGoal, @RequestParam String projectDuration,
//      @RequestParam(name = "data", required = false) List<String> data, HttpServletResponse response,
//      HttpServletRequest request) {
//    Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
//    if (organization_info == null) {
//      return MyResponse.fail("用户名不存在", 1102);
//    }
//    String[] times = projectDuration.split("-");
//    Date startDate = DateUtil.StringToDate(times[0]);
//    Date endDate = DateUtil.StringToDate(times[1]);
//    Date createDate = DateUtil.StringToDate(ReleaseTime);
//    Project_info newProject = null;
//    newProject = new Project_info(startDate, endDate, projectGoal, organization_info);
//    newProject.setCreateTime(createDate);
//    newProject.setName(projectName);
//    Set<Project_item> all_project_data = new HashSet<>();
//    for (String field : data) {
//      Project_item project_item = new Project_item(newProject, field);
//      all_project_data.add(project_item);
//      projectItemRepository.save(project_item);
//    }
//    newProject.setProjectItems(all_project_data);
//    projectInfoRepository.save(newProject);
//    log.info(organization + "创建项目:" + newProject.toString());
//    log.info("收集字段:" + data.toString());
//    return MyResponse.success();
//  }

  @PostMapping("/projectResult")
  public MyResponse reviewProjectResult(@RequestParam String projectId,@RequestParam String search){
    Project_info project_info = projectInfoRepository.findByPid(Integer.parseInt(projectId));
    if (null == project_info){
      return MyResponse.fail("所操作的数据不存在", 1002);
    }
    if (null == search){
      search = "";
    }
    Set<Agreement_info> agreement_infos = agreementInfoRepository.findAllByPid(Integer.parseInt(projectId));
    HashMap<Object,Object> data = new HashMap<>();
    data.put("agreeItems",project_info.getAgreementItems());
    data.put("projectItems",project_info.getProjectItems());
    HashSet<HashMap<String,Object>> info = new HashSet<>();
    if(agreement_infos == null){
      return MyResponse.fail("所操作的数据不存在", 1002);
    }else{
      for (Agreement_info agreement_info : agreement_infos) {
        if (agreement_info.getUsername().contains(search)){
          HashMap<String,Object> tmp = new HashMap<>();
          tmp.put("username",agreement_info.getUsername());
          HashSet<HashMap<String,Integer>> pairs = new HashSet<>();
          for (Agreement_response respons : agreement_info.getResponses()) {
            HashMap<String,Integer> tmpRes = new HashMap();
            tmpRes.put("aid",respons.getAid());
            tmpRes.put("iid",respons.getIid());
            pairs.add(tmpRes);
          }
          tmp.put("pairs",pairs);
          info.add(tmp);
        }
      }
    }
    return MyResponse.success("成功",data);
  }

//  @PutMapping("/projectDetail")
//  public MyResponse changeProject(@RequestParam String organization, @RequestParam Integer pid,
//      @RequestParam String projectName, @RequestParam String ReleaseTime, @RequestParam String projectGoal, @RequestParam String projectDuration,
//      @RequestParam(name = "data", required = false) List<String> data, HttpServletResponse response,
//      HttpServletRequest request) {
//    Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
//    if (organization_info == null) {
//      return MyResponse.fail("用户名不存在", 1102);
//    }
//    String[] times = projectDuration.split("-");
//    Date startDate = DateUtil.StringToDate(times[0]);
//    Date endDate = DateUtil.StringToDate(times[1]);
//    Date createDate = DateUtil.StringToDate(ReleaseTime);
//    Project_info newProject = null;
//    newProject = projectInfoRepository.findByPid(pid);
//    newProject.setCreateTime(createDate);
//    newProject.setName(projectName);
//    newProject.setStartTime(startDate);
//    newProject.setEndTime(endDate);
//    newProject.setPurpose(projectGoal);
//    Set<Project_item> all_project_data = newProject.getProjectItems();
//    all_project_data.clear();
//    projectItemRepository.deleteAllByPid(pid);
//    for (String field : data) {
//      Project_item project_item = new Project_item(newProject, field);
//      all_project_data.add(project_item);
//      projectItemRepository.save(project_item);
//    }
//    newProject.setProjectItems(all_project_data);
//    projectInfoRepository.save(newProject);
//    log.info(organization + "更改项目:" + newProject.toString());
//    log.info("收集字段:" + data.toString());
//    return MyResponse.success();
//  }
    @PostMapping("/projectPublish")
    public MyResponse publishProject(@RequestBody SaveProjectDraftRequest saveProjectDraftRequest) {
      Project_info project_info;
      if (saveProjectDraftRequest.getProjectId() == null || "".equals(saveProjectDraftRequest.getProjectId())) {
        project_info = new Project_info();
      } else {
        project_info = projectInfoRepository.findByPid(Integer.parseInt(saveProjectDraftRequest.getProjectId()));
        agreementItemRepository.deleteAllByPid(project_info.getPid());
        projectItemRepository.deleteAllByPid(project_info.getPid());
      }
      project_info.setOrganizationInfo(organizationInfoRepository.findByOrganization("unitname"));
      project_info.setName(saveProjectDraftRequest.getProjectName());
      project_info.setPurpose(saveProjectDraftRequest.getProjectGoal());
      String[] times = saveProjectDraftRequest.getProjectDuration().split("-");
      Date startDate = DateUtil.StringToDate(times[0]);
      Date endDate = DateUtil.StringToDate(times[1]);
      Date createDate = new Date();
      project_info.setStartTime(startDate);
      project_info.setEndTime(endDate);
      project_info.setCreateTime(createDate);
      project_info.setStatus("ongoing");
      projectInfoRepository.save(project_info);
      Set<Agreement_item> agreementItems = new HashSet<>();
      for (HashMap<String, Object> agreeItem : saveProjectDraftRequest.getAgreeItems()) {
        Agreement_item agreementItem = new Agreement_item();
        agreementItem.setIid((Integer) agreeItem.get("iid"));
        agreementItem.setName((String) agreeItem.get("name"));
        agreementItem.setValue((String) agreeItem.get("value"));
        agreementItem.setDescription((String) agreeItem.get("description"));
        agreementItem.setPid(project_info.getPid());
        agreementItem.setProjectInfo(project_info);
        agreementItemRepository.save(agreementItem);
        agreementItems.add(agreementItem);
      }
      project_info.setAgreementItems(agreementItems);
      projectInfoRepository.save(project_info);
      Set<Project_item> projectItems = new HashSet<>();
      for (HashMap<String, Object> projectItem : saveProjectDraftRequest.getProjectItems()) {
        Project_item project_item = new Project_item();
        project_item.setAid((Integer) projectItem.get("aid"));
        project_item.setName((String) projectItem.get("name"));
        project_item.setDescription((String) projectItem.get("description"));
        project_item.setProjectInfo(project_info);
        projectItemRepository.save(project_item);
        projectItems.add(project_item);
      }
      project_info.setProjectItems(projectItems);
      project_info.setReleaseTime(new Date());
      projectInfoRepository.save(project_info);
      return MyResponse.success("成功");

    }
    @PostMapping("/projectDraft")
    public MyResponse saveProjectDraft(@RequestBody SaveProjectDraftRequest saveProjectDraftRequest){
      Project_info project_info;
    if (saveProjectDraftRequest.getProjectId() == null || "".equals(saveProjectDraftRequest.getProjectId())){
      project_info = new Project_info();
    }else{
       project_info = projectInfoRepository.findByPid(Integer.parseInt(saveProjectDraftRequest.getProjectId()));
      agreementItemRepository.deleteAllByPid(project_info.getPid());
      projectItemRepository.deleteAllByPid(project_info.getPid());
    }
      project_info.setOrganizationInfo(organizationInfoRepository.findByOrganization("unitname"));
      project_info.setName(saveProjectDraftRequest.getProjectName());
      project_info.setPurpose(saveProjectDraftRequest.getProjectGoal());
      String[] times = saveProjectDraftRequest.getProjectDuration().split("-");
      Date startDate = DateUtil.StringToDate(times[0]);
      Date endDate = DateUtil.StringToDate(times[1]);
      Date createDate = new Date();
      project_info.setStartTime(startDate);
      project_info.setEndTime(endDate);
      project_info.setCreateTime(createDate);
      project_info.setStatus("draft");
      projectInfoRepository.save(project_info);
      Set<Agreement_item> agreementItems = new HashSet<>();
      for (HashMap<String, Object> agreeItem : saveProjectDraftRequest.getAgreeItems()) {
        Agreement_item agreementItem = new Agreement_item();
        agreementItem.setIid((Integer) agreeItem.get("iid"));
        agreementItem.setName((String)agreeItem.get("name"));
        agreementItem.setValue((String)agreeItem.get("value"));
        agreementItem.setDescription((String)agreeItem.get("description"));
        agreementItem.setPid(project_info.getPid());
        agreementItem.setProjectInfo(project_info);
        agreementItemRepository.save(agreementItem);
        agreementItems.add(agreementItem);
      }
      project_info.setAgreementItems(agreementItems);
      projectInfoRepository.save(project_info);
      Set<Project_item> projectItems = new HashSet<>();
      for (HashMap<String, Object> projectItem : saveProjectDraftRequest.getProjectItems()) {
        Project_item project_item = new Project_item();
        project_item.setAid((Integer) projectItem.get("aid"));
        project_item.setName((String) projectItem.get("name"));
        project_item.setDescription((String) projectItem.get("description"));
        project_item.setProjectInfo(project_info);
        projectItemRepository.save(project_item);
        projectItems.add(project_item);
      }
      project_info.setProjectItems(projectItems);
      projectInfoRepository.save(project_info);
      return MyResponse.success("成功");
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
  public MyResponse completedAgreements(@RequestParam String organization) {
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
  public MyResponse getProjectDetails(@RequestBody Map<String, Integer> map ) {
    if (null == map.get("projectId")){
      return MyResponse.fail("无效");
    }
    Integer projectId = map.get("projectId");
    Project_info projectInfo = projectInfoRepository.findByPid(projectId);
    if (projectInfo == null) {
      return MyResponse.fail("pid不存在", 1002);
    }
    HashMap<Object,Object> data = new HashMap<>();
    HashSet<String> imgUrls = new HashSet<>();
    data.put("imgUrls",imgUrls);
    data.put("projectId",String.valueOf(projectInfo.getPid()));
    data.put("projectName",projectInfo.getName());
    data.put("projectGoal",projectInfo.getPurpose());
    data.put("projectDuration",projectInfo.getStartTime()+ "-" + projectInfo.getEndTime());
    data.put("isPublished",projectInfo.getStatus().equals("ongoing") || projectInfo.getStatus().equals("finished"));
    data.put("releaseTime",projectInfo.getReleaseTime());
    data.put("description",projectInfo.getPurpose());
    HashSet<HashMap<Object,Object>> agreeItems = new HashSet<>();
    for (Agreement_item agreementItem : projectInfo.getAgreementItems()) {
      HashMap<Object,Object> item = new HashMap<>();
      item.put("iid",agreementItem.getIid());
      item.put("name",agreementItem.getName());
      item.put("value",agreementItem.getValue());
      item.put("description",agreementItem.getDescription());
      agreeItems.add(item);
    }
    data.put("agreeItems",agreeItems);
    HashSet<HashMap<Object,Object>> projectItems = new HashSet<>();
    for (Project_item projectItem : projectInfo.getProjectItems()) {
      HashMap<Object,Object> item = new HashMap<>();
      item.put("aid",projectItem.getAid());
      item.put("name",projectItem.getName());
      item.put("description",projectItem.getDescription());
      projectItems.add(item);
    }
    data.put("projectItems",projectItems);

    return MyResponse.success("成功", data);
  }

}
