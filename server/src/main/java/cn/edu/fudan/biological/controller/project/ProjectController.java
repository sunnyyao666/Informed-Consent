package cn.edu.fudan.biological.controller.project;

import cn.edu.fudan.biological.domain.Project_info;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/test")
    public MyResponse test(@RequestParam String param) {
        return MyResponse.success("成功", param);
    }

    @GetMapping("/allProjects")
    public MyResponse getAllProjects(@RequestParam("method") String method, @RequestParam("begin") String begin, @RequestParam("number") String number) {
        return getCertainProjects(method, begin, number, null);
    }

    @GetMapping("/projects")
    public MyResponse getCertainProjects(@RequestParam("method") String method, @RequestParam("begin") String begin, @RequestParam("number") String number, @RequestParam("search") String search) {
        List<Project_info> projects;
        if ("time".equals(method)) {
            if (search == null) {
                projects = projectInfoRepository.findAllByOrderByUpdateTimeDesc();
            } else {
                projects = projectInfoRepository.findAllByNameContainingOrPurposeContainingOrderByUpdateTimeDesc(search, search);
            }
        } else {
            if (search == null) {
                projects = projectInfoRepository.findAllByOrderByHotDesc();
            } else {
                projects = projectInfoRepository.findAllByNameContainingOrPurposeContainingOrderByHotDesc(search, search);
            }
        }

        int pages;
        if (projects.size() % 10 == 0) {
            pages = projects.size() / 10;
        } else {
            pages = projects.size() / 10 + 1;
        }

        List<Map<String, Object>> content = new LinkedList<>();
        int start = Integer.parseInt(begin);
        int end = start + Integer.parseInt(number);
        int i = start;
        while (i < end && i < projects.size()) {
            Project_info projectInfo = projects.get(i);
            Map<String, Object> map = new HashMap<>(4);
            map.put("projectId", projectInfo.getPid());
            map.put("projectName", projectInfo.getName());
            map.put("releaseTime", projectInfo.getReleaseTime());
            map.put("organization", projectInfo.getOrganization());
            content.add(map);
            i++;
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("pages", pages);
        result.put("content", content);
        return MyResponse.success("成功", result);
    }

    @GetMapping("/projectInfo")
    public MyResponse getProjectDetails(@RequestParam("projectId") String projectId) {
        Project_info projectInfo = projectInfoRepository.findByPid(Integer.parseInt(projectId));
        if (projectInfo == null) {
            return MyResponse.fail("pid不存在", 1002);
        }

        Map<String, Object> result = new HashMap<>(8);
        result.put("projectId", projectId);
        result.put("projectName", projectInfo.getName());
        result.put("projectGoal", projectInfo.getPurpose());
        result.put("projectDuration", projectInfo.getStartTime() + "-" + projectInfo.getEndTime());
        result.put("isPublished", "finished".equals(projectInfo.getStatus()) || "ongoing".equals(projectInfo.getStatus()));
        result.put("releaseTime", projectInfo.getReleaseTime());
        return MyResponse.success("成功", result);
    }
}

