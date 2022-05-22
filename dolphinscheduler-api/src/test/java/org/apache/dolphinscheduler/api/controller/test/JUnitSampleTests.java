package org.apache.dolphinscheduler.api.controller.test;

import org.junit.jupiter.api.extension.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class JUnitSampleTests implements TestTemplateInvocationContextProvider {
    // 是否支持该模板方法，返回false将不会执行provideTestTemplateInvocationContexts
    // (ExtensionContext context)方法，你当然可以根据上下文做些额外判断
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return IntStream.rangeClosed(1, 50) // 将会产生50次参数，意味着模板方法会被执行50次
                .mapToObj(item -> getTestTemplateInvocationContext());
    }


    /**
     * 实例化一个TestTemplateInvocationContext
     *
     * @return
     */
    private static TestTemplateInvocationContext getTestTemplateInvocationContext() {
        return new TestTemplateInvocationContext() {
            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new ParameterResolver() {
                    /**
                     * 这个地方是进行参数类型的检查
                     * @param parameterContext
                     * @param extensionContext
                     * @return
                     * @throws ParameterResolutionException
                     */
                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                        // 判断模板方法的参数类型是否是Integer，因为resolveParameter方法将返回Integer
                        Class<?> type = parameterContext.getParameter().getType();
                        //如果是Integer类型的可以,或者是String类型的也可以
                        return type.isAssignableFrom(Integer.class) || type.isAssignableFrom(String.class);
                    }

                    @Override // 产生随机整数
                    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
                        Class<?> type = parameterContext.getParameter().getType();
                        //如果是Integer类型的
                        if (type.isAssignableFrom(Integer.class)) {
                            return (int) (Math.random() * 100);
                            //如果是Integer类型的
                        } else if (type.isAssignableFrom(String.class)) {
                            return UUID.randomUUID().toString();
                        }
                        return null;
                    }
                });
            }
        };
    }
}

//final List<String> fruits = Arrays.asList("apple", "banana", "lemon");
//
//@TestTemplate
//@ExtendWith(MyTestTemplateInvocationContextProvider.class)
//void testTemplate(String fruit) {
//    assertTrue(fruits.contains(fruit));
//}
//
//public class MyTestTemplateInvocationContextProvider
//        implements TestTemplateInvocationContextProvider {
//
//    @Override
//    public boolean supportsTestTemplate(ExtensionContext context) {
//        return true;
//    }
//
//    @Override
//    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
//            ExtensionContext context) {
//
//        return Stream.of(invocationContext("apple"), invocationContext("banana"));
//    }
//
//    private TestTemplateInvocationContext invocationContext(String parameter) {
//        return new TestTemplateInvocationContext() {
//            @Override
//            public String getDisplayName(int invocationIndex) {
//                return parameter;
//            }
//
//            @Override
//            public List<Extension> getAdditionalExtensions() {
//                return Collections.singletonList(new ParameterResolver() {
//                    @Override
//                    public boolean supportsParameter(ParameterContext parameterContext,
//                            ExtensionContext extensionContext) {
//                        return parameterContext.getParameter().getType().equals(String.class);
//                    }
//
//                    @Override
//                    public Object resolveParameter(ParameterContext parameterContext,
//                            ExtensionContext extensionContext) {
//                        return parameter;
//                    }
//                });
//            }
//        };
//    }
//}