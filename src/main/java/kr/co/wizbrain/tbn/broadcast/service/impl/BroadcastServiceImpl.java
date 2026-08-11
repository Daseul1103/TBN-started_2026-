package kr.co.wizbrain.tbn.broadcast.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.wizbrain.tbn.broadcast.mapper.BroadcastMapper;
import kr.co.wizbrain.tbn.broadcast.service.BroadcastService;
import kr.co.wizbrain.tbn.broadcast.vo.BroadCastListVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadMessageVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadSearchVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadcastVO;
import kr.co.wizbrain.tbn.receipt.vo.CriteriaVO;

@Service("broadcastService")
public class BroadcastServiceImpl implements BroadcastService{
	
	@Resource(name="broadcastMapper")
	private BroadcastMapper broadcastMapper;

	@Override
	public List<BroadCastListVO> searchBroadcastList(BroadSearchVO vo) throws Exception {
		return broadcastMapper.searchBroadcastList(vo);
	}

	@Override
	public int updateBroadFlagTo(List<BroadcastVO> blist) throws Exception {
		return broadcastMapper.updateBroadFlagTo(blist);
	}
	
	@Override
	public int updateBroadFlagToAPP(List<BroadcastVO> blist) throws Exception {
		return broadcastMapper.updateBroadFlagToAPP(blist);
	}

	@Override
	public List<BroadCastListVO> selectTodaysList(CriteriaVO cri) throws Exception {
		return broadcastMapper.selectTodaysList(cri);
	}

	@Override
	public int countSearchBroadcastList(BroadSearchVO vo) throws Exception {
		return broadcastMapper.countSearchBroadcastList(vo);
	}

	@Override
	public int countBroadcastList(CriteriaVO cri) throws Exception {
		return broadcastMapper.countBroadcastList(cri);
	}

	@Override
	public int updateBroadTime(List<BroadcastVO> blist) throws Exception {
		return broadcastMapper.updateBroadTime(blist);
	}

	@Override
	public String checkBroadTime(BroadcastVO vo) throws Exception {
		return broadcastMapper.checkBroadTime(vo);
	}

	



}
