package top.cellargalaxy.util;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public class ExceptionUtilTest {
	@Test
	public void pringException() throws Exception {
		Exception exception=new Exception();
		System.out.println(ExceptionUtil.pringException(exception));
	}
	
}