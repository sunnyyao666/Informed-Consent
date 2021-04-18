package cn.edu.fudan.biological.task;

import cn.edu.fudan.biological.domain.Project_info;
import cn.edu.fudan.biological.repository.ProjectInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: biological
 * @description: 根据时间决定结束任务
 * @author: Shen Zhengyu
 * @create: 2021-04-18 09:45
 **/
@Component
@Slf4j
public class AutoChangeProjectStatusTask {
    private final ProjectInfoRepository projectInfoRepository;
    @Autowired
    public AutoChangeProjectStatusTask(ProjectInfoRepository projectInfoRepository) {
        this.projectInfoRepository = projectInfoRepository;
    }

    @Autowired

//    @Scheduled(cron = "0 0 0 */1 * ?")
    @Scheduled(cron = "0 /1 ?")
    public void execute() throws InterruptedException {
        log.info("开始执行周期任务，将过期项目改变状态");
        log.info("线程休眠3秒");
        Thread.sleep(3000);
        for (Project_info project_info : projectInfoRepository.findAll()) {
            if ("ongoing".equals(project_info.getStatus())){
                Date finishDate = project_info.getEndTime();
                Date nowDate = new Date();
                if (nowDate.after(finishDate)){
                    project_info.setStatus("finished");
                    projectInfoRepository.save(project_info);
                }
            }
        }
    }
}
