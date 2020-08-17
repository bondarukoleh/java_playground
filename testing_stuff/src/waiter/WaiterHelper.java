import org.codehaus.jackson.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

/* Should help you to wait for some condition, or action. Just pass the function that will return you the actualResult,
* and the expectedResult that you needed. Configurable via WaitOptions */

@Slf4j
public class WaiterHelper<T> {
    public boolean waitForCondition(IFunctionToRun<T> condition, T expectedResult, WaitOptions options) throws InterruptedException, JsonProcessingException {
        log.info("Start waiting for \"{}\"", expectedResult);
        long startTime = System.currentTimeMillis();
        T actualResult = null;
        try {
            do {
                actualResult = condition.runFunction();
                log.info("result of the function is \"{}\"", actualResult);
                if (actualResult.equals(expectedResult)) {
                    return true;
                }
                Thread.sleep(options.waitInterval);
            } while ((System.currentTimeMillis() - startTime) < options.waitTime);
        } catch (Exception e) {
            if (options.throwError) {
                throw new Error(options.errorMessage + " " + e.getMessage());
            }
        }
        if (options.throwError) {
            throw new Error(String.format("Couldn't get the expected result \"%s\". Actual result is \"%s\"", expectedResult, actualResult));
        }
        log.info("Silently finish the waiting. Couldn't get the expected result");
        return false;
    }
}
