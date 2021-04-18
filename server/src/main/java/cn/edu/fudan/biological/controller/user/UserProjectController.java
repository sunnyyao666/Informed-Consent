package cn.edu.fudan.biological.controller.user;

import cn.edu.fudan.biological.controller.request.user.UserProjectRequest;
import cn.edu.fudan.biological.domain.*;
import cn.edu.fudan.biological.dto.MyResponse;
import cn.edu.fudan.biological.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @program: biological
 * @description: 用户项目相关功能
 * @author: Yao Hongtao
 * @create: 2021-03-15 15:53
 **/
@RestController
@RequestMapping("/api/user")
public class UserProjectController {
    private final UserInfoRepository userInfoRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectItemRepository projectItemRepository;
    private final AgreementItemRepository agreementItemRepository;
    private final AgreementInfoRepository agreementInfoRepository;
    private final AgreementResponseRepository agreementResponseRepository;

    @Autowired
    public UserProjectController(UserInfoRepository userInfoRepository, ProjectInfoRepository projectInfoRepository, ProjectItemRepository projectItemRepository, AgreementItemRepository agreementItemRepository, AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository) {
        this.userInfoRepository = userInfoRepository;
        this.projectInfoRepository = projectInfoRepository;
        this.projectItemRepository = projectItemRepository;
        this.agreementItemRepository = agreementItemRepository;
        this.agreementInfoRepository = agreementInfoRepository;
        this.agreementResponseRepository = agreementResponseRepository;
    }

    @RequestMapping(value = "/agreements", method = {RequestMethod.POST, RequestMethod.PUT})
    public MyResponse submitAgreement(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        Integer pid = userProjectRequest.getProjectId();
        Project_info projectInfo = projectInfoRepository.findById(pid).orElse(null);
        if (projectInfo == null) {
            return MyResponse.fail("projectId不存在", 1002);
        }

        Agreement_info agreementInfo = agreementInfoRepository.findByUsernameAndPid(username, pid);
        if (agreementInfo == null) {
            agreementInfo = new Agreement_info(userInfo, projectInfo);
            agreementInfoRepository.save(agreementInfo);
        }

        Agreement_response[] data = userProjectRequest.getData();
        for (Agreement_response r : data) {
            Integer aid = r.getAid();
            Project_item projectItem = projectItemRepository.findByPidAndAid(pid, aid);
            if (projectItem == null) {
                return MyResponse.fail("aid不存在", 1002);
            }

            Integer iid = r.getIid();
            Agreement_item agreementItem = agreementItemRepository.findByPidAndIid(pid, iid);
            if (agreementItem == null) {
                return MyResponse.fail("iid不存在", 1002);
            }

            Agreement_response response = agreementResponseRepository.findByAgreementIdAndAid(agreementInfo.getId(), aid);
            if (response != null) {
                response.setAgreementItem(agreementItem);
            } else {
                response = new Agreement_response(agreementInfo, projectItem, agreementItem);
            }
            agreementResponseRepository.save(response);
        }

        return MyResponse.success();
    }

    @GetMapping("/agreements")
    public MyResponse getAgreement(@RequestParam("username") String username, @RequestParam("projectId") String projectId) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        int pid = Integer.parseInt(projectId);
        Project_info projectInfo = projectInfoRepository.findById(pid).orElse(null);
        if (projectInfo == null) {
            return MyResponse.fail("projectId不存在", 1002);
        }


        Map<String, Object> result = new HashMap<>(8);
        result.put("projectId", projectId);
        result.put("isFinished", "finished".equals(projectInfo.getStatus()));
        result.put("items", agreementItemRepository.findAllByPidOrderByIid(pid));
        result.put("agreements", projectItemRepository.findAllByPidOrderByAid(pid));

        Agreement_info agreementInfo = agreementInfoRepository.findByUsernameAndPid(username, pid);
        if (agreementInfo == null) {
            result.put("pairs", new LinkedList<>());
        } else {
            result.put("pairs", agreementResponseRepository.findAllByAgreementIdOrderByAid(agreementInfo.getId()));
        }
        return MyResponse.success("成功", result);
    }

    @GetMapping("/projects")
    public MyResponse getPagedProjects(@RequestParam("username") String username, @RequestParam("onpage") Integer onpage, @RequestParam("finishpage") Integer finishpage) {
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        List<Agreement_info> agreements = agreementInfoRepository.findAllByUsernameOrderByPid(username);
        List<Project_info> ongoingProjects = new LinkedList<>();
        List<Project_info> finishedProjects = new LinkedList<>();
        for (Agreement_info agreement : agreements) {
            Project_info projectInfo = projectInfoRepository.findById(agreement.getPid()).orElse(null);
            if ("ongoing".equals(projectInfo.getStatus())) {
                ongoingProjects.add(projectInfo);
            } else {
                finishedProjects.add(projectInfo);
            }
        }

        int onPages;
        if (ongoingProjects.size() % 3 == 0) {
            onPages = ongoingProjects.size() / 3;
        } else {
            onPages = ongoingProjects.size() / 3 + 1;
        }

        int finishPages;
        if (finishedProjects.size() % 3 == 0) {
            finishPages = finishedProjects.size() / 3;
        } else {
            finishPages = finishedProjects.size() / 3 + 1;
        }

        List<Project_info> ongoingList = new LinkedList<>();
        int i = (onpage - 1) * 3;
        while (i < ongoingProjects.size() && i < onpage * 3) {
            ongoingList.add(ongoingProjects.get(i));
            i++;
        }

        List<Project_info> finishedList = new LinkedList<>();
        i = (finishpage - 1) * 3;
        while (i < finishedProjects.size() && i < finishpage * 3) {
            finishedList.add(finishedProjects.get(i));
            i++;
        }

        Map<String, Object> result = new HashMap<>(4);
        result.put("onPages", onPages);
        result.put("finishedPages", finishPages);
        result.put("ongoingList", ongoingList);
        result.put("finishedList", finishedList);
        return MyResponse.success("成功", result);
    }
}

