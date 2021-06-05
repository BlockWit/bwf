package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.TaskViewMapper;
import com.blockwit.bwf.model.tasks.SchedTaskStatus;
import com.blockwit.bwf.repository.SchedTasksRepository;
import com.blockwit.bwf.service.SchedTasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/panel/tasks")
public class SchedTasksController {

  @Autowired
  SchedTasksRepository schedTasksRepository;

  @Autowired
  SchedTasksService schedTasksService;

  @Autowired
  TaskViewMapper taskViewMapper;

  @GetMapping
  public ModelAndView appPanelTasks() {
    return new ModelAndView("redirect:/panel/tasks/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelTasksPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/tasks"),
        pageNumber,
        schedTasksRepository,
        taskViewMapper);
  }

  @GetMapping("/task/{taskId}/{action}")
  public ModelAndView changeTaskStatus(HttpServletRequest request,
                                       RedirectAttributes redirectAttributes,
                                       @PathVariable("taskId") long taskId,
                                       @PathVariable("action") String action) {
    SchedTaskStatus newSchedTaskStatus;
    SchedTaskStatus oldSchedTaskStatus;
    if (action.equals("run")) {
      oldSchedTaskStatus = SchedTaskStatus.STS_STOPPED;
      newSchedTaskStatus = SchedTaskStatus.STS_PREPARING_RUN;
    } else if (action.equals("stop")) {
      oldSchedTaskStatus = SchedTaskStatus.STS_RUNNING;
      newSchedTaskStatus = SchedTaskStatus.STS_PREPARING_STOP;
    } else {
      redirectAttributes.addFlashAttribute("message_error", "Can't perform action " + action + "!");
      return new ModelAndView("redirect:/panel/tasks");
    }

    schedTasksService.tryChangeStatus(taskId, oldSchedTaskStatus, newSchedTaskStatus).fold(
        error -> redirectAttributes.addFlashAttribute("message_error", error.getDescr())
        , task -> redirectAttributes.addFlashAttribute("message_success", "Task " + taskId + " successfully updated")
    );

    return new ModelAndView("redirect:/panel/tasks");
  }

  @GetMapping("/all/{action}")
  public ModelAndView changeTasksStatus(HttpServletRequest request,
                                        RedirectAttributes redirectAttributes,
                                        @PathVariable("action") String action) {
    SchedTaskStatus newSchedTaskStatus;
    SchedTaskStatus oldSchedTaskStatus;
    if (action.equals("run")) {
      oldSchedTaskStatus = SchedTaskStatus.STS_STOPPED;
      newSchedTaskStatus = SchedTaskStatus.STS_PREPARING_RUN;
    } else if (action.equals("stop")) {
      oldSchedTaskStatus = SchedTaskStatus.STS_RUNNING;
      newSchedTaskStatus = SchedTaskStatus.STS_PREPARING_STOP;
    } else {
      redirectAttributes.addFlashAttribute("message_error", "Can't perform action " + action + "!");
      return new ModelAndView("redirect:/panel/tasks");
    }

    schedTasksService.tryChangeStatusAll(oldSchedTaskStatus, newSchedTaskStatus).fold(
        error -> redirectAttributes.addFlashAttribute("message_error", error.getDescr())
        , task -> redirectAttributes.addFlashAttribute("message_success", "All tasks successfully updated")
    );

    return new ModelAndView("redirect:/panel/tasks");
  }

}
