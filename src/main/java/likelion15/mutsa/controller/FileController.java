package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpServletResponse;
import likelion15.mutsa.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/notice/{id}/download/{fileId}")
    public String download(
            @PathVariable("id") Long id,
            @PathVariable("fileId") Long fileId,
            Model model,
            HttpServletResponse response
    )throws IOException {

//        model.addAttribute(
//                "file",
//                fileService.readFile(fileId));

        fileService.downFile(fileService.readFile(id), response);


        return null;
    }

    @RequestMapping("/board/{id}/download/{fileId}")
    public String download2(
            @PathVariable("id") Long id,
            @PathVariable("fileId") Long fileId,
            Model model,
            HttpServletResponse response
    )throws IOException {

//        model.addAttribute(
//                "file",
//                fileService.readFile(fileId));

//        System.out.println(fileService.readFile(fileId));
        fileService.downFile(fileService.readFile(id), response);


        return null;
    }

}
