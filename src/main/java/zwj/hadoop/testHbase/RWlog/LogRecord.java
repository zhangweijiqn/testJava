/**
 * 
 */
package zwj.hadoop.testHbase.RWlog;

/**
 * @author bjzhongdegen
 *
 */
public class LogRecord {
	private Long nextTimestamp = -1L;
	
	private String content;

	private Long lastLineTimestamp;

	public Long getLastLineTimestamp() {
		return lastLineTimestamp;
	}

	public void setLastLineTimestamp(Long lastLineTimestamp) {
		this.lastLineTimestamp = lastLineTimestamp;
	}

	public Long getNextTimestamp() {
		return nextTimestamp;
	}

	public void setNextTimestamp(Long nextTimestamp) {
		this.nextTimestamp = nextTimestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
