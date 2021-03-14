package cn.edu.fudan.biological.controller.organization;

import cn.edu.fudan.biological.domain.Organization_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.OrganizationInfoRepository;
import cn.edu.fudan.biological.repository.ProjectDataRepository;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import cn.edu.fudan.biological.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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
    DateUtil dateUtil = new DateUtil();
    @Autowired
    public OrganizationProjectController(OrganizationInfoRepository organizationInfoRepository, ProjectInfoRepository projectInfoRepository, ProjectDataRepository projectDataRepository) {
        this.organizationInfoRepository = organizationInfoRepository;
        this.projectInfoRepository = projectInfoRepository;
        this.projectDataRepository = projectDataRepository;
    }

    @PostMapping("/project")
    public MyResponse createProject(@RequestParam String organization, @RequestParam String time, HttpServletResponse response, HttpServletRequest request) {
        Organization_info organization_info = organizationInfoRepository.findByOrganization(organization);
        if(organization_info == null){
            return MyResponse.fail("用户名不存在",1102);
        }else{
            dateUtil.StringToDate(time);
            return MyResponse.success();
        }
    }
}
