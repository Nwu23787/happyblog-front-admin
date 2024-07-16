package com.happyblog.mappers;
import java.util.List;
import org.apache.ibatis.annotations.Param;


 interface BaseMapper<T, P> {
	/**
	 * 
	 * insert:(插入). <br/>
	 *
	 * @author luohaili
	 * @param t
	 * @return
	 * @since JDK 1.7
	 */
	 Integer insert(@Param("bean") T t);


	/**
	 *
	 * insertOrUpdate:(插入或者更新). <br/>
	 *
	 * @author luohaili
	 * @param t
	 * @return
	 * @since JDK 1.7
	 */
	 Integer insertOrUpdate(@Param("bean") T t);


	/**
	 * 
	 * insertBatch:(批量插入). <br/>
	 *
	 * @author luohaili
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	 Integer insertBatch(@Param("list") List<T> list);


/**
	 *
	 * insertOrUpdateBatch:(批量插入或更新). <br/>
	 *
	 * @author luohaili
	 * @param list
	 * @return
	 * @since JDK 1.7
	 */
	 Integer insertOrUpdateBatch(@Param("list") List<T> list);


	/**
	 * 
	 * selectList:(根据参数查询集合). <br/>
	 *
	 * @author luohaili
	 * @param p
	 * @return
	 * @since JDK 1.7
	 */
	 List<T> selectList(@Param("query") P p);

	/**
	 * 
	 * selectCount:(根据集合查询数量). <br/>
	 *
	 * @author luohaili
	 * @param t
	 * @return
	 * @since JDK 1.7
	 */
	 Integer selectCount(@Param("query") P p);

}
