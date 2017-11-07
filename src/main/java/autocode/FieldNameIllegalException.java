package autocode;
/**
 * @Title: FieldNameIllegalException.java
 * @Package com.csi.ro.bizfoundation.core.autocode
 * @Description: 标志字段名称不符合JavaBean规范的异常
 * @author moishalo.zhang moishalo.zhang@gmail.com
 * @date 2012-12-11 上午11:22:33
 * @version V1.0
 */
public class FieldNameIllegalException extends RuntimeException {

	private static final long serialVersionUID = 1558322149273066052L;

    public FieldNameIllegalException() {
    	super();
    }

    
    public FieldNameIllegalException(String message) {
	super(message);
    }

    
    public FieldNameIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    
    public FieldNameIllegalException(Throwable cause) {
        super(cause);
    }
}
