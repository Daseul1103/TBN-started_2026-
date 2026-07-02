package kr.co.wizbrain.tbn.event.service.impl;

import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.event.mapper.EventMapper;
import kr.co.wizbrain.tbn.event.service.EventService;
import kr.co.wizbrain.tbn.event.vo.EventVO;
import kr.co.wizbrain.tbn.event.vo.eFileVO;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;


@Service("eventService")
public class EventServiceImpl implements EventService{
	@Resource(name="eventMapper")
	private EventMapper eventMapper;

	@Override
	public List<EventVO> getEventList(EventVO paramVO) {
		return eventMapper.getEventList(paramVO);
	}
	

	@Override
	public List<EventVO> getFileList(EventVO paramVO) {
		return eventMapper.getFileList(paramVO);
	}
	
	@Override
	public List getEventList(ParamsDto params) {
		return eventMapper.getEventList2(params);
	}

	@Override
	public int getAllEventListSize(EventVO paramVO) {
		return eventMapper.getAllEventListSize(paramVO);
	}

	@Override
	public List<InfrmVO> getAttendanceList(EventVO paramVO) {
		List<InfrmVO> list = eventMapper.getAttendanceList(paramVO);
		
		// 개인 정보 암호화 (이름, 전화번호, 주소)
		try {
	        for(InfrmVO vo : list){

	            if(vo.getInformerName() != null && !"".equals(vo.getInformerName()))
	                vo.setInformerName(decryptAES(vo.getInformerName()));

	            if(vo.getPhoneCell() != null && !"".equals(vo.getPhoneCell()))
	                vo.setPhoneCell(decryptAES(vo.getPhoneCell()));
	            
	            if(vo.getAddressHome() != null && !"".equals(vo.getAddressHome()))
	                vo.setAddressHome(decryptAES(vo.getAddressHome()));

	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }
		
		return list;
		
	}

	@Override
	public List<InfrmVO> getInformerList4Event(EventVO paramVO) {
		
		if ("informerName".equals(paramVO.getSearchType())
		        && paramVO.getSearchValue() != null
		        && !"".equals(paramVO.getSearchValue())) {

		    try {
		        paramVO.setSearchValue(
		            encryptAES(paramVO.getSearchValue().trim())
		        );
		    } catch (Exception e) {
		        throw new RuntimeException("검색어 암호화 실패", e);
		    }
		}
		
		if ("phoneCell".equals(paramVO.getSearchType())
		        && paramVO.getSearchValue() != null
		        && !"".equals(paramVO.getSearchValue())) {

		    try {
		        paramVO.setSearchValue(
		            encryptAES(paramVO.getSearchValue().trim())
		        );
		    } catch (Exception e) {
		        throw new RuntimeException("검색어 암호화 실패", e);
		    }
		}
		
		List<InfrmVO> list = eventMapper.getInformerList4Event(paramVO);
		
		// 개인 정보 암호화 (이름, 전화번호, 주소)
		try {
	        for(InfrmVO vo : list){

	            if(vo.getInformerName() != null && !"".equals(vo.getInformerName()))
	                vo.setInformerName(decryptAES(vo.getInformerName()));

	            if(vo.getPhoneCell() != null && !"".equals(vo.getPhoneCell()))
	                vo.setPhoneCell(decryptAES(vo.getPhoneCell()));
	            
	            if(vo.getAddressHome() != null && !"".equals(vo.getAddressHome()))
	                vo.setAddressHome(decryptAES(vo.getAddressHome()));

	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }
		
		return list;
	}

	@Override
	public int saveEvent(EventVO paramVO) {
		int cnt = 0;
		cnt= eventMapper.updateEvent(paramVO);
		if(cnt == 0){
			cnt = eventMapper.saveEvent(paramVO);
		}
		return cnt;
	}

	@Override
	public String selectEventId() {
		return eventMapper.selectEventId();
	}
	
	@Override
	public void insertFile(List<eFileVO> inputVo) {
		eventMapper.insertFile(inputVo);
	}
	
	
	@Override
	public int saveAttendance(EventVO thvo, List<String> alist) {
		int cnt = 0;
		cnt = eventMapper.saveAttendance(thvo,alist);
//		cnt= eventMapper.updateAttendance(thvo,alist);
//		if(cnt == 0){
//			cnt = eventMapper.saveAttendance(thvo,alist);
//		}
		return cnt;
	}

	@Override
	public int deleteEvent(EventVO paramVO) {
		int cnt = eventMapper.deleteAttendance(paramVO); // 이벤트 참석자 삭제
		cnt += eventMapper.deleteEvent(paramVO); // 이벤트 삭제
		
		return cnt;
	}
	
	@Override
	public void deleteFileOne(String fileId) {
		eventMapper.deleteFileOne(fileId);
	}

	@Override
	public String selectFileName(String fileId) {
		return eventMapper.selectFileName(fileId);
	}
	
	@Override
	public int deleteAttendance(EventVO paramVO) {
		int cnt = eventMapper.deleteAttendance(paramVO); // 이벤트 참석자 삭제
		return cnt;
	}


	@Override
	public int updateEvent(EventVO paramVO) {
		int cnt = eventMapper.updateEvent(paramVO); // 이벤트 참석자 삭제
		return cnt;
	}

	@Override
	public List<EventVO> getEvtList(EventVO paramVO) {
		return eventMapper.getEvtList(paramVO);
	}

	
	@Override
	public List<EventVO> selectFileList(EventVO paramVO){
		return eventMapper.selectFileList(paramVO);
	}
	
	@Override
	public List<EventVO> selectFileList2(EventVO paramVO){
		return eventMapper.selectFileList2(paramVO);
	}
	
	@Override
	public void deleteFile(EventVO paramVO) {
		eventMapper.deleteFile(paramVO);
	}
	
	
	
	private String encryptAES(String plainText) throws Exception {

	    String secretKey = "1234567890123456";

	    SecretKeySpec keySpec =
	            new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

	    Cipher cipher = Cipher.getInstance("AES");

	    cipher.init(Cipher.ENCRYPT_MODE, keySpec);

	    byte[] encrypted =
	            cipher.doFinal(plainText.getBytes("UTF-8"));

	    return Base64.getEncoder().encodeToString(encrypted);
	}
	
	
	private String decryptAES(String encryptedText) throws Exception {

	    String secretKey = "1234567890123456";

	    SecretKeySpec keySpec =
	            new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

	    Cipher cipher = Cipher.getInstance("AES");

	    cipher.init(Cipher.DECRYPT_MODE, keySpec);

	    byte[] decoded =
	            Base64.getDecoder().decode(encryptedText);

	    return new String(cipher.doFinal(decoded), "UTF-8");
	}
}