package com.tzcpa.exceptions;
/**
 * <p>Description: 获取值异常</p>
 * @author WTL
 * @date 2019年5月10日
 */
public class GetValueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3678095937165138748L;
	
	public GetValueException() {
        super();
    }
	
	public GetValueException(String message) {
        super(message);
    }

}

