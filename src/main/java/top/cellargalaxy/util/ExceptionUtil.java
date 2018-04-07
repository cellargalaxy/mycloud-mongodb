package top.cellargalaxy.util;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public class ExceptionUtil {
	public static final String pringException(Exception exception) {
		if (exception == null) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(exception.toString());
		StackTraceElement[] stackElements = exception.getStackTrace();
		if (stackElements != null) {
			for (StackTraceElement stackElement : stackElements) {
				stringBuilder.append("\n\tat " + stackElement.getClassName() + '.' + stackElement.getMethodName() + '(' + stackElement.getFileName() + ':' + stackElement.getLineNumber() + ')');
			}
		}
		return stringBuilder.toString();
	}
}
