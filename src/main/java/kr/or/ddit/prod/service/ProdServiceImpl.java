package kr.or.ddit.prod.service;

import java.util.List;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.exception.UserNotFoundException;
import kr.or.ddit.prod.dao.ProdDAO;
import kr.or.ddit.prod.dao.ProdDAOImpl;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProdServiceImpl implements ProdService {
	private ProdDAO prodDAO = new ProdDAOImpl();
	@Override
	public ProdVO retriveProd(String prodId) {
		ProdVO selectProd = prodDAO.selectProd(prodId);
		if(selectProd == null) {
			throw new RuntimeException(String.format(prodId+"에 해당하는 상품 없음."));
		}
	
		return selectProd;
		
		
	}
	@Override
	public void retrieveProdList(PagingVO<ProdVO> pagingVO) { //CALL BY REFERENCE
		pagingVO.setTotalRecord(prodDAO.selectTotalRecord(pagingVO));

		List<ProdVO> ProdList = prodDAO.selectProdList(pagingVO);
		pagingVO.setDataList(ProdList);
		log.info("ProdList : {}",ProdList);
		ProdList.stream()
		.forEach(System.out::println); // 메소드 레퍼런스 구조

	}
	@Override
	public ServiceResult createProd(ProdVO prod) {
		ServiceResult result = null;
			
		int rowcnt = prodDAO.insertProd(prod);
			
		result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;

		return result;
	}
	@Override
	public ServiceResult modifyProd(ProdVO prod) {
		
		ServiceResult result = null;
		retriveProd(prod.getProdId());
		int rowcnt = prodDAO.updateProd(prod);
		result = rowcnt > 0 ? ServiceResult.OK : ServiceResult.FAIL;
		

		return result;
	}

}
