package cn.edu.fudan.biological.controller.project;

import cn.edu.fudan.biological.controller.organization.OrganizationProjectController;
import cn.edu.fudan.biological.controller.request.user.UserProjectRequest;
import cn.edu.fudan.biological.domain.Project_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: biological
 * @description: 用户+单位的项目功能
 * @author: Yao Hongtao
 * @create: 2021-03-16 15:14
 **/
@RestController
@RequestMapping("/api")
public class ProjectController {
    private final ProjectInfoRepository projectInfoRepository;

    @Autowired
    public ProjectController(ProjectInfoRepository projectInfoRepository) {
        this.projectInfoRepository = projectInfoRepository;
    }

    @GetMapping("/allProjects")
    public MyResponse getAllProjects(@RequestParam("method") String method, @RequestParam("begin") String begin, @RequestParam("number") String number) {
        List<Project_info> projects = projectInfoRepository.findAllByOrderByHotDesc();
        if ("time".equals(method)) {
            projects = projectInfoRepository.findAllByOrderByUpdateTimeDesc();
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("content", projects);
        return MyResponse.success("成功", result);
    }

    @GetMapping("/projectInfo")
    public MyResponse getProjectDetails(@RequestParam("projectId") String projectId) {
        Project_info projectInfo = projectInfoRepository.findByPid(Integer.parseInt(projectId));
        if (projectInfo == null) {
            return MyResponse.fail("pid不存在", 1002);
        }
        return MyResponse.success("成功", projectInfo);
    }
}

