package org.apache.dolphinscheduler.api.controller.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;

class RecordStateOnErrorExtension implements LifecycleMethodExecutionExceptionHandler {

    @Override
    public void handleBeforeAllMethodExecutionException(ExtensionContext context, Throwable ex)
            throws Throwable {
//        memoryDumpForFurtherInvestigation("Failure recorded during class setup");
        throw ex;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable ex)
            throws Throwable {
//        memoryDumpForFurtherInvestigation("Failure recorded during test setup");
        throw ex;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable ex)
            throws Throwable {
//        memoryDumpForFurtherInvestigation("Failure recorded during test cleanup");
        throw ex;
    }

    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable ex)
            throws Throwable {
//        memoryDumpForFurtherInvestigation("Failure recorded during class cleanup");
        throw ex;
    }
}

//@ExtendWith(ThirdExecutedHandler.class)
//class MultipleHandlersTestCase {
//
//    // Register handlers for @Test, @BeforeEach, @AfterEach only
//    @ExtendWith(SecondExecutedHandler.class)
//    @ExtendWith(FirstExecutedHandler.class)
//    @Test
//    void testMethod() {
//    }
//
//}