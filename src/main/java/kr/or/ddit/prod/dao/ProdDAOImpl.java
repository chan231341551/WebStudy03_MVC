package kr.or.ddit.prod.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.or.ddit.member.dao.MemberDAO;
import kr.or.ddit.mybatis.MyBatisUtils;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.ProdVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProdDAOImpl implements ProdDAO {
	
	private SqlSessionFactory sqlSessionFactory = MyBatisUtils.getsqlSessionFactory();
	@Override
	public ProdVO selectProd(String prodId) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
			){
				ProdDAO mapperProxy = sqlSession.getMapper(ProdDAO.class);
				return mapperProxy.selectProd(prodId);
				
			}
	
	}
	@Override
	public int selectTotalRecord(PagingVO<ProdVO> pagingVO) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
			){
			ProdDAO mapperProxy = sqlSession.getMapper(ProdDAO.class);
				return mapperProxy.selectTotalRecord(pagingVO);
				
			}
	
	}
	@Override
	public List<ProdVO> selectProdList(PagingVO<ProdVO> pagingVO) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
			){
				ProdDAO mapperProxy = sqlSession.getMapper(ProdDAO.class);
				return mapperProxy.selectProdList(pagingVO);
				
			}
	
	
	}
	@Override
	public int insertProd(ProdVO prod) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
			){
				ProdDAO mapperProxy = sqlSession.getMapper(ProdDAO.class);
				int rowcnt = mapperProxy.insertProd(prod);
				sqlSession.commit();
				return rowcnt;
				
			}
	}
	@Override
	public int updateProd(ProdVO prod) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
			){
				ProdDAO mapperProxy = sqlSession.getMapper(ProdDAO.class);
				int rowcnt = mapperProxy.updateProd(prod);
				sqlSession.commit();
				return rowcnt;
			}
	}

}
