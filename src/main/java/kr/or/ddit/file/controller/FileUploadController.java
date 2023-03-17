package kr.or.ddit.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FileUploadController {
	
	@RequestMapping(value="/file/upload.do",method=RequestMethod.POST)
//	@postMapping("/file/upload.do")
	public String upload(HttpServletRequest req, HttpSession session) throws IOException, ServletException {
		String textPart = req.getParameter("textPart");
		String numPart = req.getParameter("numPart");
		String datePart = req.getParameter("datePart");
		log.info("textPart : {}",textPart);
		log.info("numPart : {}",numPart);
		log.info("datePart : {}",datePart);
		session.setAttribute("textPart", textPart);
		session.setAttribute("numPart", numPart);
		session.setAttribute("datePart", datePart);
		
		String saveFolderURL = "/resources/prodImages";
		ServletContext application = req.getServletContext();
		String saveFolderPath = application.getRealPath(saveFolderURL);
		File saveFolder = new File(saveFolderPath);
		if(!saveFolder.exists()) { 
			saveFolder.mkdirs();
		}
		
		List<String> metadata = req.getParts().stream()
					  .filter((p)-> // p -> parts 중 하나
						  p.getContentType() != null && p.getContentType().startsWith("image/")// 업로드한파일만 적용
					  )
					  .map((p)->{ 
						  String originalFilename = p.getSubmittedFileName(); 
						  // 원본파일명으로는 저장하지않음 
						
						  String saveFilename = UUID.randomUUID().toString();
						  File saveFile = new File(saveFolder,saveFilename);
						  try {
							p.write(saveFile.getCanonicalPath());
							String saveFileURL = saveFolderURL + "/" + saveFilename; // /resources/prodImages + / + 파일이름
							return saveFileURL;
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					  }).collect(Collectors.toList());
		
		session.setAttribute("fileMetadata", metadata);
		return "redirect:/fileupload/uploadForm.jsp";
	}
}
