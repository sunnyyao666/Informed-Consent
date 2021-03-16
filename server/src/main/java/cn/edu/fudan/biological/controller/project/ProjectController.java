package cn.edu.fudan.biological.controller.project;

import cn.edu.fudan.biological.controller.request.user.UserProjectRequest;
import cn.edu.fudan.biological.domain.Project_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

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
    public MyResponse getAllProjects(@RequestBody UserProjectRequest userProjectRequest) {
        String method = userProjectRequest.getMethod();
        Integer offset = userProjectRequest.getOffset();
        Integer sum = userProjectRequest.getSum();
        List<Project_info> projects = projectInfoRepository.findAllByOrderByUpdateTimeDesc();
        if ("hot".equals(method)) projects = projectInfoRepository.findAllByOrderByHotDesc();

        List<Project_info> result = new LinkedList<>();
        int i = offset;
        int n = offset + sum;
        while (i < n && i < projects.size()) {
            result.add(projects.get(i));
            i++;
        }
        return MyResponse.success("成功", result);
    }

    @GetMapping("/projectDetails")
    public MyResponse getProjectDetails(@RequestBody UserProjectRequest userProjectRequest) {
        Integer pid = userProjectRequest.getPid();
        Project_info projectInfo = projectInfoRepository.findByPid(pid);
        if (projectInfo == null) return MyResponse.fail("pid不存在", 1002);

        return MyResponse.success("成功", projectInfo);
    }
}

