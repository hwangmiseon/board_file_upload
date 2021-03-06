package com.study.data.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.study.attach.service.IAttachDao;
import com.study.attach.vo.AttachVO;
import com.study.data.service.IDataBoardDao;
import com.study.data.service.IDataBoardService;
import com.study.data.vo.DataBoardVO;
import com.study.data.vo.DataSearchVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;

@Service
public class DataBoardServiceImpl implements IDataBoardService {

	// FreeBoardServiceImpl 은 IFreeBoardDao 객체인
	// freeBoardDao에 의존한다.
   
	@Inject
	private IDataBoardDao dataDao;

	@Inject
	private IAttachDao attachDao;

	@Override
	public List<DataBoardVO> getBoardList(DataSearchVO searchVO) {
		int totalRowCount = dataDao.getBoardCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		return dataDao.getBoardList(searchVO);
	}

	@Override
	public DataBoardVO getBoard(int boNo) throws BizNotFoundException {
		// 두번째 방법 : 메인 dao에서 join을 통해 한번에 결과 조회 
		DataBoardVO data = dataDao.getBoard(boNo);
		if (data == null) {
			throw new BizNotFoundException();
		}
		
		// 첫번째 방법 : 각각의 dao를 통해 결과 조회
//		List<AttachVO> atts = attachDao.getAttachByParentNoList(boNo, "DATA");
//		data.setAttaches(atts);
		return data;
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		int cnt = dataDao.increaseHit(boNo);
		if (cnt < 1) {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public void modifyBoard(DataBoardVO free)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		DataBoardVO board = dataDao.getBoard(free.getBoNo());
		if (board == null)
			throw new BizNotFoundException();
		if (board.getBoPass().equals(free.getBoPass())) {
			System.out.println("modify");
			int cnt = dataDao.updateBoard(free);
			if (cnt < 1) throw new BizNotEffectedException();
			
			// 기존 삭제된 첨부파일 번호가 있는지 확인
			int[] dels = free.getDelAtchNos();
			if(dels != null) {
				attachDao.deleteAttaches(dels);
			}
			
			// 첨부파일이 존재하는 경우 첨부파일 등록 , parentNo 설정 필요
			List<AttachVO> atchList = free.getAttaches();
			if (atchList != null) {
				for (AttachVO vo : atchList) {
					vo.setAtchParentNo(free.getBoNo());
					attachDao.insertAttach(vo);
				}
			}
		} else {
			throw new BizPasswordNotMatchedException();
		}
	}

	@Override
	public void removeBoard(DataBoardVO free)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		DataBoardVO board = dataDao.getBoard(free.getBoNo());
		if (board == null)
			throw new BizNotFoundException();
		if (board.getBoPass().equals(free.getBoPass())) {
			int cnt = dataDao.deleteBoard(free);
			if (cnt < 1)
				throw new BizNotEffectedException();
		} else {
			throw new BizPasswordNotMatchedException();
		}
	}

	@Override
	public void registBoard(DataBoardVO free) throws BizNotEffectedException {
		int cnt = dataDao.insertBoard(free);
		if (cnt < 1) {
			throw new BizNotEffectedException();
		}

		// 첨부파일이 존재하는 경우 첨부파일 등록 , parentNo 설정 필요
		List<AttachVO> atchList = free.getAttaches();
		if (atchList != null) {
			for (AttachVO vo : atchList) {
				vo.setAtchParentNo(free.getBoNo());
				attachDao.insertAttach(vo);
			}
		}
	}

}
