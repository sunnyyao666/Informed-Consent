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
    private final UserStarRepository userStarRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectDataRepository projectDataRepository;
    private final AgreementInfoRepository agreementInfoRepository;
    private final AgreementResponseRepository agreementResponseRepository;

    @Autowired
    public UserProjectController(UserInfoRepository userInfoRepository, UserStarRepository userStarRepository, ProjectInfoRepository projectInfoRepository, ProjectDataRepository projectDataRepository, AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userStarRepository = userStarRepository;
        this.projectInfoRepository = projectInfoRepository;
        this.projectDataRepository = projectDataRepository;
        this.agreementInfoRepository = agreementInfoRepository;
        this.agreementResponseRepository = agreementResponseRepository;
    }

    @PostMapping("/agreement")
    public MyResponse submitAgreement(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        Integer pid = userProjectRequest.getPid();
        Project_info projectInfo = projectInfoRepository.findByPid(pid);
        if (projectInfo == null) return MyResponse.fail("pid不存在", 1002);

        Agreement_info agreementInfo = agreementInfoRepository.findByUsernameAndPid(username, pid);
        if (agreementInfo == null) {
            agreementInfo = new Agreement_info(userInfo, projectInfo);
            agreementInfoRepository.save(agreementInfo);
        }

        List<Agreement_response> data = userProjectRequest.getData();
        for (Agreement_response r : data) {
            Integer dataId = r.getDataId();
            Project_data projectData = projectDataRepository.findByDataId(dataId);
            if (projectData == null) return MyResponse.fail("dataId不存在", 1002);

            Agreement_response response = agreementResponseRepository.findByAgreementIdAndDataId(agreementInfo.getId(), dataId);
            if (response != null) response.setResponse(r.getResponse());
            else response = new Agreement_response(agreementInfo, projectData, r.getResponse());
            agreementResponseRepository.save(response);
        }

        return MyResponse.success();
    }

    @GetMapping("/userProjects")
    public MyResponse getSavedAgreements(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        List<Agreement_info> agreements = agreementInfoRepository.findAllByUsernameOrderByPid(username);
        List<Project_info> onGoingList = new LinkedList<>();
        List<Project_info> finishedList = new LinkedList<>();
        for (Agreement_info agreement : agreements) {
            Project_info projectInfo = projectInfoRepository.findByPid(agreement.getPid());
            if ("ongoing".equals(projectInfo.getStatus())) onGoingList.add(projectInfo);
            else finishedList.add(projectInfo);
        }
        Map<String, List<Project_info>> result = new HashMap<>();
        result.put("onGoingList", onGoingList);
        result.put("finishedList", finishedList);
        return MyResponse.success("成功", result);
    }

    @PostMapping("/collectedProject")
    public MyResponse collectProject(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) return MyResponse.fail("用户名不存在", 1102);

        Integer pid = userProjectRequest.getPid();
        Project_info projectInfo = projectInfoRepository.findByPid(pid);
        if (projectInfo == null) return MyResponse.fail("pid不存在", 1002);

        User_star user_star = userStarRepository.findByUsernameAndPid(username, pid);
        if (user_star == null) {
            user_star = new User_star(userInfo, projectInfo);
            userStarRepository.save(user_star);
        } else userStarRepository.delete(user_star);
        return MyResponse.success();
    }
}

