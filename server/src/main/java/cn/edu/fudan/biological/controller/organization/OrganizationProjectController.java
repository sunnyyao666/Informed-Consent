package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.controller.request.unit.SaveProjectDraftRequest;
import cn.edu.fudan.biological.domain.*;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.*;
import cn.edu.fudan.biological.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.text.SimpleDateFormat;
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
      AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository,
      AgreementItemRepository agreementItemRepository) {
    this.organizationInfoRepository = organizationInfoRepository;
    this.projectInfoRepository = projectInfoRepository;
    this.projectItemRepository = projectItemRepository;
    this.agreementInfoRepository = agreementInfoRepository;
    this.agreementResponseRepository = agreementResponseRepository;
    this.agreementItemRepository = agreementItemRepository;

  }
  SimpleDateFormat pointformat = new SimpleDateFormat("yyyy.MM.dd");
  SimpleDateFormat hiformat = new SimpleDateFormat("yyyy-MM-dd");
  @GetMapping("/projects")
  public MyResponse getAllProjectsOfUnit(@RequestParam("unitname") String unitname) {
    Organization_info organization_info = organizationInfoRepository.findByOrganization(unitname);
    HashMap<Object, Object> data = new HashMap<>();
    HashSet<HashMap<Object, Object>> publishedList = new HashSet<>();
    HashSet<HashMap<Object, Object>> draftList = new HashSet<>();
    for (Project_info project : organization_info.getProjects()) {
      HashMap<Object, Object> tmp = new HashMap<>();
      tmp.put("projectId", String.valueOf(project.getId()));
      tmp.put("projectName", project.getName());

      tmp.put("joinTime",hiformat.format(project.getEndTime()).replace("-","."));
      if (null != project.getReleaseTime()){
        tmp.put("releaseTime", hiformat.format(project.getReleaseTime()).replace("-","."));
      }else{
        tmp.put("releaseTime",null);
      }
      if (project.getStatus().equals("draft")) {
        draftList.add(tmp);
      } else {
        publishedList.add(tmp);
      }
    }
    data.put("publishedList", publishedList);
    data.put("draftList", draftList);
    return MyResponse.success("成功请求" + unitname + "的所有项目", data);
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

  @GetMapping("/projectResult")
  public MyResponse reviewProjectResult(@RequestParam("projectId") String projectId,@RequestParam("search") String search) {
    Project_info project_info = projectInfoRepository.findById(Integer.parseInt(projectId)).orElse(null);
    if (null == project_info) {
      return MyResponse.fail("所操作的数据不存在", 1002);
    }

    Set<Agreement_info> agreement_infos = agreementInfoRepository.findAllByPid(Integer.parseInt(projectId));
    if (null != search && !"".equals(search)){
      agreement_infos = agreementInfoRepository.findAllByUsernameContainingAndPid(search,Integer.parseInt(projectId));
    }

    HashMap<Object, Object> data = new HashMap<>();
    data.put("agreeItems", project_info.getAgreementItems());
    data.put("projectItems", project_info.getProjectItems());
    HashSet<HashMap<String, Object>> info = new HashSet<>();
    if (agreement_infos == null) {
      return MyResponse.fail("所操作的数据不存在", 1002);
    } else {
      for (Agreement_info agreement_info : agreement_infos) {
          HashMap<String, Object> tmp = new HashMap<>();
          tmp.put("username", agreement_info.getUsername());
          HashSet<HashMap<String, Integer>> pairs = new HashSet<>();
          for (Agreement_response respons : agreement_info.getResponses()) {
            HashMap<String, Integer> tmpRes = new HashMap();
            tmpRes.put("aid", respons.getAid());
            tmpRes.put("iid", respons.getIid());
            pairs.add(tmpRes);
          }
          tmp.put("pairs", pairs);
          info.add(tmp);

      }
    }
    return MyResponse.success("成功", data);
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
  @Transactional
  @PostMapping("/projectPublish")
  public MyResponse publishProject(@RequestBody SaveProjectDraftRequest saveProjectDraftRequest) {
    //只有在数据库存在项目&&是draft，或不存在项目才能发布
    Project_info project_info;
    if (saveProjectDraftRequest.getProjectId() == null || "".equals(saveProjectDraftRequest.getProjectId())) {
      Set<Project_info> tmps = projectInfoRepository.findAllByName(saveProjectDraftRequest.getProjectName());
      for (Project_info tmp : tmps) {
        if (null != tmp && !tmp.getStatus().equals("draft")) {
          log.warn("不能发布同名项目");
          return MyResponse.fail("不能发布同名项目");
        }
      }

      project_info = new Project_info();

    } else {
      //项目已经存在，它必须是draft，且同一个人
      project_info = projectInfoRepository.findById(Integer.parseInt(saveProjectDraftRequest.getProjectId())).orElse(null);
      if (null == project_info) {
        log.warn("指定的项目草稿不存在");
        return MyResponse.fail("指定的项目草稿不存在");
      }
      if (!project_info.getStatus().equals("draft")) {
        log.warn("该PID指定的项目已不是草稿状态");
        return MyResponse.fail("该PID指定的项目已不是草稿状态");
      }
      if (!project_info.getOrganization().equals(saveProjectDraftRequest.getUnitname())) {
        log.warn("指定PID发布:权限错误");
        return MyResponse.fail("指定PID发布:权限错误");
      }
      if (!projectInfoRepository.findAllByNameAndStatus(saveProjectDraftRequest.getProjectName(),"ongoing").isEmpty()){
        log.warn("已有重名进行中项目");
        return MyResponse.fail("已有重名进行中项目");
      }
      if (!projectInfoRepository.findAllByNameAndStatus(saveProjectDraftRequest.getProjectName(),"finished").isEmpty()){
        log.warn("已有重名已结束项目");
        return MyResponse.fail("已有重名已结束项目");
      }
      agreementItemRepository.deleteAllByPid(project_info.getId());
      projectItemRepository.deleteAllByPid(project_info.getId());
    }
    project_info.setOrganization(
        organizationInfoRepository.findByOrganization(saveProjectDraftRequest.getUnitname()).getOrganization());
    project_info
        .setOrganizationInfo(organizationInfoRepository.findByOrganization(saveProjectDraftRequest.getUnitname()));
    project_info.setName(saveProjectDraftRequest.getProjectName());
    project_info.setPurpose(saveProjectDraftRequest.getProjectGoal());
    String[] times = saveProjectDraftRequest.getProjectDuration().split("-");
    //点字符串 到 杠 日期
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
      agreementItem.setPid(project_info.getId());
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
    log.info(project_info.getName() + "项目发布成功");
    return MyResponse.success("成功");

  }

  @Transactional
  @PostMapping("/projectDraft")
  public MyResponse saveProjectDraft(@RequestBody SaveProjectDraftRequest saveProjectDraftRequest) {
    Project_info project_info;
    String tmpName = saveProjectDraftRequest.getProjectName();
    Set<Project_info> tmpProjects = projectInfoRepository.findAllByName(tmpName);
    //修改旧草稿
    for (Project_info tmpProject : tmpProjects) {
      if (null != tmpProject && !tmpProject.getStatus().equals("draft")) {
        log.warn("已有同名项目");
        return MyResponse.fail("已有同名项目");
      }
      if (null != tmpProject && tmpProject.getStatus().equals("draft") && tmpProject.getOrganization()
          .equals(saveProjectDraftRequest.getUnitname()) && !String.valueOf(tmpProject.getId()).equals(saveProjectDraftRequest.getProjectId())) {
        log.warn("本单位已有同名项目草稿" + tmpProject.getName());
        return MyResponse.fail("本单位已有同名项目草稿" + tmpProject.getName()) ;
      }
    }

    //创建新草稿
    if (saveProjectDraftRequest.getProjectId() == null || "".equals(saveProjectDraftRequest.getProjectId())) {
      project_info = new Project_info();
    } else {
      project_info = projectInfoRepository.findById(Integer.parseInt(saveProjectDraftRequest.getProjectId())).orElse(null);
      if (null == project_info) {
        log.warn("指定的项目草稿不存在");
        return MyResponse.fail("指定的项目草稿不存在");
      }
      if (!project_info.getStatus().equals("draft")) {
        log.warn("不能修改已发布的项目");
        return MyResponse.fail("不能修改已发布的项目");
      }
      if (!project_info.getOrganization().equals(saveProjectDraftRequest.getUnitname())) {
        log.warn("指定PID发布:权限错误");
        return MyResponse.fail("指定PID发布:权限错误");
      }
      project_info.getAgreementItems().clear();
      project_info.getProjectItems().clear();
      projectInfoRepository.save(project_info);
      agreementItemRepository.deleteAllByPid(project_info.getId());
      projectItemRepository.deleteAllByPid(project_info.getId());
    }
    project_info.setOrganization(
        organizationInfoRepository.findByOrganization(saveProjectDraftRequest.getUnitname()).getOrganization());
    project_info
        .setOrganizationInfo(organizationInfoRepository.findByOrganization(saveProjectDraftRequest.getUnitname()));
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
      agreementItem.setName((String) agreeItem.get("name"));
      agreementItem.setValue((String) agreeItem.get("value"));
      agreementItem.setDescription((String) agreeItem.get("description"));
      agreementItem.setPid(project_info.getId());
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
    log.info(project_info.getOrganization() + "单位:" + project_info.getName() + "项目草稿存储成功");
    return MyResponse.success(project_info.getOrganization() + "单位:" + project_info.getName() + "项目草稿存储成功");
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
      int pid = project_info.getId();
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

  @GetMapping("/projectInfo")
  public MyResponse getProjectDetails(@RequestParam String projectId) {
    if (null == projectId || "".equals(projectId)) {
      return MyResponse.fail("无效");
    }
    Project_info projectInfo = projectInfoRepository.findById(Integer.parseInt(projectId)).orElse(null);
    if (projectInfo == null) {
      return MyResponse.fail("pid不存在", 1002);
    }
    HashMap<Object, Object> data = new HashMap<>();
    HashSet<String> imgUrls = new HashSet<>();
    data.put("imgUrls", imgUrls);
    data.put("projectId", String.valueOf(projectInfo.getId()));
    data.put("projectName", projectInfo.getName());
    data.put("projectGoal", projectInfo.getPurpose());
    data.put("projectDuration", hiformat.format(projectInfo.getStartTime()).replace("-",".") + "-" + hiformat.format(projectInfo.getEndTime()).replace("-","."));
    data.put("isPublished", projectInfo.getStatus().equals("ongoing") || projectInfo.getStatus().equals("finished"));
    if (null != projectInfo.getReleaseTime()){
      data.put("releaseTime", hiformat.format(projectInfo.getReleaseTime()).replace("-","."));

    }else{
      data.put("releaseTime",null);
    }
    data.put("description", projectInfo.getPurpose());
    HashSet<HashMap<Object, Object>> agreeItems = new HashSet<>();
    for (Agreement_item agreementItem : projectInfo.getAgreementItems()) {
      HashMap<Object, Object> item = new HashMap<>();
      item.put("iid", agreementItem.getIid());
      item.put("name", agreementItem.getName());
      item.put("value", agreementItem.getValue());
      item.put("description", agreementItem.getDescription());
      agreeItems.add(item);
    }
    data.put("agreeItems", agreeItems);
    HashSet<HashMap<Object, Object>> projectItems = new HashSet<>();
    for (Project_item projectItem : projectInfo.getProjectItems()) {
      HashMap<Object, Object> item = new HashMap<>();
      item.put("aid", projectItem.getAid());
      item.put("name", projectItem.getName());
      item.put("description", projectItem.getDescription());
      projectItems.add(item);
    }
    data.put("projectItems", projectItems);

    return MyResponse.success("成功", data);
  }

}
