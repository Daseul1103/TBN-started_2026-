package kr.co.wizbrain.tbn.broadcast.mapper;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.wizbrain.tbn.broadcast.vo.BroadCastListVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadMessageVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadSearchVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadcastVO;
import kr.co.wizbrain.tbn.receipt.vo.CriteriaVO;

@Mapper("broadcastMapper")
public interface BroadcastMapper{
	public int countBroadcastList(CriteriaVO cri) throws Exception;
	public List<BroadCastListVO> selectTodaysList(CriteriaVO cri) throws Exception;
	public List<BroadCastListVO> searchBroadcastList(BroadSearchVO vo) throws Exception;
	public int countSearchBroadcastList(BroadSearchVO vo) throws Exception;
	public String checkBroadTime(BroadcastVO vo) throws Exception;
	public int updateBroadTime(List<BroadcastVO> blist) throws Exception;
	public int updateBroadFlagTo(List<BroadcastVO> blist) throws Exception;
	public int updateBroadFlagToAPP(List<BroadcastVO> blist) throws Exception; 
}
