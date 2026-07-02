package kr.co.wizbrain.tbn.event.service;

import java.util.List;

import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.event.vo.EventVO;
import kr.co.wizbrain.tbn.event.vo.eFileVO;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

public interface EventService{

	List<EventVO> getEventList(EventVO paramVO);
	List<EventVO> getFileList(EventVO paramVO);
	List<EventVO> getEvtList(EventVO paramVO);
	
	List<EventVO> selectFileList(EventVO paramVO);
	List<EventVO> selectFileList2(EventVO paramVO);
	
	
	void deleteFile(EventVO paramVO);
	
	List getEventList(ParamsDto params);

	int getAllEventListSize(EventVO paramVO);

	List<InfrmVO> getAttendanceList(EventVO paramVO);

	List<InfrmVO> getInformerList4Event(EventVO paramVO);

	int saveEvent(EventVO paramVO);

	int saveAttendance(EventVO paramVO, List<String> alist);
	
	int deleteEvent(EventVO paramVO);
	
	void deleteFileOne(String fileId);
	
	String selectFileName(String fileId);
	
	int deleteAttendance(EventVO paramVO);

	int updateEvent(EventVO paramVO);

	String selectEventId();
	
	
	public void insertFile(List<eFileVO> inputVo);
}