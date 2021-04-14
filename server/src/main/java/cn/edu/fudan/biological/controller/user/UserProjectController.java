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
    private final AgreementInfoRepository agreementInfoRepository;
    private final AgreementResponseRepository agreementResponseRepository;

    @Autowired
    public UserProjectController(UserInfoRepository userInfoRepository, ProjectInfoRepository projectInfoRepository, ProjectItemRepository projectItemRepository, AgreementInfoRepository agreementInfoRepository, AgreementResponseRepository agreementResponseRepository) {
        this.userInfoRepository = userInfoRepository;
        this.projectInfoRepository = projectInfoRepository;
        this.projectItemRepository = projectItemRepository;
        this.agreementInfoRepository = agreementInfoRepository;
        this.agreementResponseRepository = agreementResponseRepository;
    }

    @PostMapping("/agreement")
    public MyResponse submitAgreement(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        Integer pid = userProjectRequest.getPid();
        Project_info projectInfo = projectInfoRepository.findByPid(pid);
        if (projectInfo == null) {
            return MyResponse.fail("pid不存在", 1002);
        }

        Agreement_info agreementInfo = agreementInfoRepository.findByUsernameAndPid(username, pid);
        if (agreementInfo == null) {
            agreementInfo = new Agreement_info(userInfo, projectInfo);
            agreementInfoRepository.save(agreementInfo);
        }

//        List<Agreement_response> data = userProjectRequest.getData();
//        for (Agreement_response r : data) {
//            Integer dataId = r.getAid();
//            Project_item projectData = projectItemRepository.findByDataId(dataId);
//            if (projectData == null) {
//                return MyResponse.fail("dataId不存在", 1002);
//            }
//
//            Agreement_response response = agreementResponseRepository.findByAgreementIdAndDataId(agreementInfo.getId(), dataId);
//            if (response != null) {
//                response.setResponse(r.getResponse());
//            } else {
//                response = new Agreement_response(agreementInfo, projectData, r.getResponse());
//            }
//            agreementResponseRepository.save(response);
//        }

        return MyResponse.success();
    }

    @GetMapping("/userProjects")
    public MyResponse getSavedAgreements(@RequestBody UserProjectRequest userProjectRequest) {
        String username = userProjectRequest.getUsername();
        User_info userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            return MyResponse.fail("用户名不存在", 1102);
        }

        List<Agreement_info> agreements = agreementInfoRepository.findAllByUsernameOrderByPid(username);
        List<Project_info> onGoingList = new LinkedList<>();
        List<Project_info> finishedList = new LinkedList<>();
        for (Agreement_info agreement : agreements) {
            Project_info projectInfo = projectInfoRepository.findByPid(agreement.getPid());
            if ("ongoing".equals(projectInfo.getStatus())) {
                onGoingList.add(projectInfo);
            } else {
                finishedList.add(projectInfo);
            }
        }
        Map<String, List<Project_info>> result = new HashMap<>(2);
        result.put("onGoingList", onGoingList);
        result.put("finishedList", finishedList);
        return MyResponse.success("成功", result);
    }
}

