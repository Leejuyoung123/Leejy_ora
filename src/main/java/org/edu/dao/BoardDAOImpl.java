package org.edu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.edu.vo.BoardTypeVO;
import org.edu.vo.BoardVO;
import org.edu.vo.PageVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BoardDAOImpl implements IF_BoardDAO {

	private static String mapperQuery = "org.edu.dao.IF_BoardDAO";
	
	@Inject
	private SqlSession sqlSession;

	@Override
	public void insertBoard(BoardVO boardVO) throws Exception {
		sqlSession.insert(mapperQuery + ".insertBoard", boardVO);		
	}

	@Override
	public List<BoardVO> selectBoard(PageVO pageVO) throws Exception {
		return sqlSession.selectList(mapperQuery + ".selectBoard", pageVO);
	}

	@Override
	public void updateBoard(BoardVO boardVO) throws Exception {
		sqlSession.update(mapperQuery + ".updateBoard", boardVO);
	}

	@Override
	public void deleteBoard(Integer bno) throws Exception {
		sqlSession.delete(mapperQuery + ".deleteBoard", bno);
	}

	@Override
	public BoardVO viewBoard(Integer bno) throws Exception {
		return sqlSession.selectOne(mapperQuery + ".viewBoard", bno);
	}

	@Override
	public void insertAttach(String fullName , Integer bno) throws Exception {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bno", bno);
		paramMap.put("fullName", fullName);
		sqlSession.insert(mapperQuery+".insertAttach",paramMap);
	}

	@Override
	public List<String> selectAttach(Integer bno) throws Exception {
		return sqlSession.selectList(mapperQuery + ".selectAttach", bno);
		}

	@Override
	public void deleteAttach(Integer bno) throws Exception {
			sqlSession.delete(mapperQuery + ".deleteAttach", bno);
	}

	@Override
	public void updateAttach(String fullName, Integer bno) throws Exception {
		Map<String , Object> paramMap = new HashMap<String ,Object>();
		paramMap.put("bno", bno);
		paramMap.put("fullName", fullName);
		sqlSession.insert(mapperQuery+".updateAttach", paramMap);
			
	}

	@Override
	public int countBno(PageVO pageVO) throws Exception {
		
		return sqlSession.selectOne(mapperQuery + ".countBno",pageVO);
	}

	@Override
	public void updateViewCount(Integer bno) throws Exception {
		sqlSession.selectOne(mapperQuery + ".updateViewCount" , bno);
	}

	@Override
	public void deleteBoardReply(Integer bno) throws Exception {
		sqlSession.delete(mapperQuery + ".deleteBoardReply", bno);
	}

	@Override
	public int selectTopBno() throws Exception {
		return sqlSession.selectOne(mapperQuery+".selectTopBno");
	}

	@Override
	public List<BoardTypeVO> selectBoardType() throws Exception {
		return sqlSession.selectList(mapperQuery+".selectBoardType");
	}

	@Override
	public void insertBoardType(BoardTypeVO boardTypeVO) throws Exception {
			sqlSession.insert(mapperQuery +".insertBoardType" , boardTypeVO);
	}

	@Override
	public void updateBoardType(BoardTypeVO boardTypeVO) throws Exception {
		sqlSession.update(mapperQuery + ".updateBoardType" , boardTypeVO);
	}

	@Override
	public void deleteBoardType(String bod_type) throws Exception {
		sqlSession.delete(mapperQuery+ ".deleteBoardType" , bod_type);
	}

	@Override
	public BoardTypeVO viewBoardType(String bod_type) throws Exception {
		return sqlSession.selectOne(mapperQuery+".viewBoardType",bod_type);
	}

}