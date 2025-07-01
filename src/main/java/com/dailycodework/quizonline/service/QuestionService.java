package com.dailycodework.quizonline.service;

import com.dailycodework.quizonline.model.Question;
import com.dailycodework.quizonline.repository.QuestionRepository;
import com.dailycodework.quizonline.transformer.QuizTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Simpson Alfred
 */

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService{
    private final QuestionRepository questionRepository;
    private final QuizTransformer quizTransformer;
    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<String> getAllSubjects() {
        return questionRepository.findDistinctSubject();
    }

    @Override
    public Question updateQuestion(Long id, Question question) throws ChangeSetPersister.NotFoundException {
        Optional<Question> theQuestion = this.getQuestionById(id);
        if (theQuestion.isPresent()){
            Question updatedQuestion = theQuestion.get();
            updatedQuestion.setQuestion(question.getQuestion());
            updatedQuestion.setChoices(question.getChoices());
            updatedQuestion.setCorrectAnswers(question.getCorrectAnswers());
            return questionRepository.save(updatedQuestion);
        }else {
            throw new ChangeSetPersister.NotFoundException();
        }

    }
    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
    @Override
    public List<Question> getQuestionsForUser(Integer numOfQuestions, String subject) {
        Pageable pageable = PageRequest.of(0, numOfQuestions);
        return questionRepository.findBySubject(subject, pageable).getContent();
    }

    public List<Question> getAllQuizQuestions(String operation) {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(question -> quizTransformer.transformResponse(question, operation)).toList();
    }

    public int insertQuestions(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be null or empty");
        }
        //questionRepository.deleteAll();
        return questionRepository.saveAll(questions).size();
    }

    @Override
    public List<Question> getAllQuizQuestionsBySubject(String subject) {
        List<Question> questions = questionRepository.findBySubject(subject);
        return questions.stream().map(question -> quizTransformer.transformResponse(question, "ANSWER")).toList();
    }

    @Override
    public List<Question> deleteAllQuizQuestionsBySubject(String subject) {
        List<Question> questions = questionRepository.deleteBySubject(subject);
        return questions.stream().map(question -> quizTransformer.transformResponse(question, "ANSWER")).toList();
    }

    @Override
    public int insertQuestions(String subject, List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be null or empty");
        }
        //questionRepository.deleteAll();
        //List<Question> questionEntities = quizTransformer.transformRequestToEntity(subject, questions);
        return questionRepository.saveAll(questions).size();
    }
}
