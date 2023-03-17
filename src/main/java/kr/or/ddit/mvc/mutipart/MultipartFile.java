package kr.or.ddit.mvc.mutipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface MultipartFile {
	public byte[] getBytes() throws IOException;
	public String getContentType();
	public InputStream getInputStream() throws IOException;
	public String getName();
	public String getOriginalFilename();
	public long getSize();
	public boolean isEmpty(); 
	public void transferTO(File dest) throws IOException;
}
