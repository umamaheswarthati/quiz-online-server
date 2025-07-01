package com.dailycodework.quizonline.transformer;


import com.dailycodework.quizonline.model.Question;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class QuizTransformer {
    public Question transformResponse(Question question, String operation) {
        Question dto = new Question();
        dto.setId(question.getId());
        dto.setSubject(question.getSubject());
        dto.setQuestionType(question.getQuestionType());
        dto.setQuestion(question.getQuestion());
        dto.setChoices(question.getChoices());
        // Manually assemble the options list from individual columns
        //dto.setChoices(convertToEntityAttribute(question.getChoices()));
        if ("ANSWER".equalsIgnoreCase(operation)) {
            dto.setCorrectAnswers(question.getCorrectAnswers());
            //dto.setExplanation(question.getExplanation());
        }
        return dto;
    }

    /*public List<String> convertToEntityAttribute(List<String> dbData) {        // Convert String from database back to
        // List<String> for the entity
        //return dbData != null && !dbData.trim().isEmpty() ? Arrays.asList(dbData.split("")) : Collections.emptyList();
        // Return an empty list instead of null
    }*/

    /*public List<Question> transformRequestToEntity(List<Question> questionRequests) {
        return questionRequests.stream().map(questionRequest -> {
            Question question = new Question();
            question.setSubject(questionRequest.getSubject());
            question.setQuestion(questionRequest.getQuestion());
            question.setChoices(questionRequest.getChoices());
            question.setCorrectAnswers(questionRequest.getCorrectAnswers());
            //question.setExplanation(questionRequest.getExplanation());
            return question;
        }).toList();
    }*/

    public List<Question> transformRequestToEntity(String subject, List<Question> questionRequests) {
        return questionRequests.stream().map(questionRequest -> {
            Question question = new Question();
            question.setQuestionType(question.getQuestionType());
            question.setSubject(subject);
            question.setQuestion(questionRequest.getQuestion());
            question.setChoices(questionRequest.getChoices());
            question.setCorrectAnswers(questionRequest.getCorrectAnswers());
            //question.setExplanation(questionRequest.getExplanation());
            return question;
        }).toList();
    }
}
