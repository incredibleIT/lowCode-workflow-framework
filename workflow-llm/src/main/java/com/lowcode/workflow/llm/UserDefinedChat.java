package com.lowcode.workflow.llm;


import dev.langchain4j.agent.tool.P;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface UserDefinedChat {

        /**
         * 与用户进行交互
         * @param userInput 用户输入
         * @return 模型回复
         */
        @SystemMessage("你是一个智能助手, 可以回答用户提出的问题, 当用户的问题涉及到数据库操作时, 你根据用户的问题如果涉及到数据库操作, 你必须调用mysql工具来执行数据库操作")
        String chat(@UserMessage String userInput);
}
