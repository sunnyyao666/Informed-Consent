package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.domain.*;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.*;
import cn.edu.fudan.biological.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    public OrganizationProjectController(OrganizationInfoRepository organizationInfoRepository, ProjectInfoRepository projectInfoRepository, ProjectDataRepository projectDataRepository, AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository) {
        this.organizationInfoRepository = organizationInfoRepository;
        this.projectInfoRepository = projectInfoRepository;
        this.projectDataRepository = projectDataRepository;
        this.agreementInfoRepository = agreementInfoRepository;
        this.agreementResponseRepository = agreementResponseRepository;
    }


    @PostMapping("/project")
    public MyResponse createProject(@RequestParam String organization,@RequestParam String pid, @RequestParam String name, @RequestParam String time, @RequestParam String purpose, @RequestParam String schedule, @RequestParam(name = "data",required = false) List<String> fields, HttpServletResponse response, HttpServletRequest request) {
        Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
        if(organization_info == null){
            return MyResponse.fail("用户名不存在",1102);
        }
        String[] times = schedule.split("-");
        Date startDate = DateUtil.StringToDate(times[0]);
        Date endDate = DateUtil.StringToDate(times[1]);
        Date createDate = DateUtil.StringToDate(time);
        Project_info newProject = null;
        if(null == pid) {
            newProject = new Project_info(startDate, endDate, purpose, organization_info);
            newProject.setCreateTime(createDate);
            newProject.setName(name);
            Set<Project_data> all_project_data = new HashSet<>();
            for (String field : fields) {
                Project_data project_data = new Project_data(newProject,field);
                all_project_data.add(project_data);
                projectDataRepository.save(project_data);
            }
            newProject.setData(all_project_data);
        }else{
            newProject = projectInfoRepository.findByPid(pid);
            newProject.setCreateTime(createDate);
            newProject.setName(name);
            newProject.setStartTime(startDate);
            newProject.setEndTime(endDate);
            newProject.setPurpose(purpose);
            Set<Project_data> all_project_data = newProject.getData();
            all_project_data.clear();
            projectDataRepository.deleteAllByPid(pid);
            for (String field : fields) {
                Project_data project_data = new Project_data(newProject,field);
                all_project_data.add(project_data);
                projectDataRepository.save(project_data);
            }
            newProject.setData(all_project_data);
        }
        projectInfoRepository.save(newProject);
        return MyResponse.success();
    }
    @PostMapping("/savedProject")
    public MyResponse savedProject(@RequestParam String organization,@RequestParam String pid, @RequestParam String name, @RequestParam String time, @RequestParam String purpose, @RequestParam String schedule, @RequestParam(name = "data",required = false) List<String> fields, HttpServletResponse response, HttpServletRequest request) {
        if(null == pid){
            return createProject(organization, pid,  name,  time,  purpose,  schedule,  fields,  response,  request);
        }else{
            Project_info content = projectInfoRepository.findByPid(pid);
            if(null == content){
                return MyResponse.fail("所操作的数据不存在",1002);
            }else{
                return MyResponse.success("成功",content);
            }
        }
    }

    @PostMapping("/projects")
    public MyResponse projects(@RequestParam String organization,@RequestParam String method, @RequestParam int number, HttpServletResponse response, HttpServletRequest request) {
        List<Project_info> project_infos = projectInfoRepository.findAllByOrganizationAndStatusOrderByName(organization,method);
        List<Project_info> content = new ArrayList<>();
        int i = 1;
        int nums = 0;
        for (Project_info project_info : project_infos) {
            if(i < number){
                break;
            }else{
                nums++;
                content.add(project_info);
                if(nums >= 10){
                    break;
                }
            }
            i++;
        }
        return MyResponse.success("成功",content);
    }
    @PostMapping("/completedAgreements")
    public MyResponse completedAgreements(@RequestParam String organization,@RequestParam String method, @RequestParam int number, HttpServletResponse response, HttpServletRequest request) {
        Set<Project_info> project_infos = projectInfoRepository.findAllByOrganization(organization);
        Set<Agreement_response> datas = new HashSet<>();
        for (Project_info project_info : project_infos) {
            int pid = project_info.getPid();
            Set<Agreement_info> agreement_infos = agreementInfoRepository.findAllByPid(pid);
            for (Agreement_info agreement_info : agreement_infos) {
                Set<Agreement_response> responses = agreement_info.getResponses();
                datas.addAll(responses);
            }
        }
        ArrayList<Agreement_response> datass = new ArrayList<>(datas);
        datass.sort(new Comparator<Agreement_response>() {
            @Override
            public int compare(Agreement_response o1, Agreement_response o2) {
                return o1.getAgreementId()-o2.getAgreementId();
            }
        });
        ArrayList<Agreement_response> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int toGet = i+number-1;
            if (toGet >= datass.size()){
                break;
            }else{
                data.add(datass.get(i+number-1));
            }
        }
        return MyResponse.success("成功",data);
    }

}
