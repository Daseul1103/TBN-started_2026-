package kr.co.wizbrain.tbn.event.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.event.vo.EventVO;
import kr.co.wizbrain.tbn.event.vo.eFileVO;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

@Mapper("eventMapper")
public interface EventMapper{
	List<EventVO> getEventList(EventVO paramVO);
	
	List<EventVO> getFileList(EventVO paramVO);
	
	List getEventList2(ParamsDto paramVO);

	int getAllEventListSize(EventVO paramVO);

	List<InfrmVO> getAttendanceList(EventVO paramVO);

	List<InfrmVO> getInformerList4Event(EventVO paramVO);

	int saveEvent(EventVO paramVO);
	
	int updateEvent(EventVO paramVO);

	int saveAttendance(@Param("thvo")EventVO thvo, @Param("chkArr")List<String> chkArr);
	
	String selectEventId();
	
	int deleteEvent(EventVO paramVO);
	
	void deleteFileOne(String fileId);
	
	public String selectFileName(String fileId);
	
	int deleteAttendance(EventVO paramVO);

	int updateAttendance(@Param("thvo")EventVO thvo, @Param("chkArr")List<String> chkArr);
	
	List<EventVO> getEvtList(EventVO paramVO);
	
	public List<EventVO> selectFileList(EventVO paramVO);
	public List<EventVO> selectFileList2(EventVO paramVO);
	
	void insertFile(List<eFileVO> inputVo);
	
	void deleteFile(EventVO paramVO);
}