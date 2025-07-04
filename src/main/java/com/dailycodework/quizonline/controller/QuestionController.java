package com.dailycodework.quizonline.controller;

import com.dailycodework.quizonline.model.Question;
import com.dailycodework.quizonline.service.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author Simpson Alfred
 */

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuestionController {
    private final IQuestionService questionService;

    @PostMapping("/create-new-question")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question){
        Question createdQuestion = questionService.createQuestion(question);
        return ResponseEntity.status(CREATED).body(createdQuestion);
    }

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Question> theQuestion = questionService.getQuestionById(id);
        if (theQuestion.isPresent()){
            return ResponseEntity.ok(theQuestion.get());
        }else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @PutMapping("/question/{id}/update")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id, @RequestBody Question question) throws ChangeSetPersister.NotFoundException {
        Question updatedQuestion = questionService.updateQuestion(id, question);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/question/{id}/delete")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id){
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects(){
        List<String> subjects = questionService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/quiz/fetch-questions-for-user")
    public ResponseEntity<List<Question>> getQuestionsForUser(
            @RequestParam Integer numOfQuestions, @RequestParam String subject){
        List<Question> allQuestions = questionService.getQuestionsForUser(numOfQuestions, subject);

        List<Question> mutableQuestions = new ArrayList<>(allQuestions);
        Collections.shuffle(mutableQuestions);

        int availableQuestions = Math.min(numOfQuestions, mutableQuestions.size());
        List<Question> randomQuestions = mutableQuestions.subList(0, availableQuestions);
        return ResponseEntity.ok(randomQuestions);
    }

    @GetMapping("/v2/questions")
    public ResponseEntity<List<Question>> getAllQuestionsV2() {
        List<Question> questions = questionService.getAllQuizQuestions(null);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/v2/questions/{operation}")
    public ResponseEntity<List<Question>> getAllQuestions(@PathVariable String operation) {
        List<Question> questions = questionService.getAllQuizQuestions(operation);
        return ResponseEntity.ok(questions);
    }


    @GetMapping("/v2/questions/subject/{subject}")
    public ResponseEntity<List<Question>> getAllQuestionsBySubject(@PathVariable String subject) {
        List<Question> questions = questionService.getAllQuizQuestionsBySubject(subject);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/v2/questions/bulk/insert")
    public ResponseEntity<String> insertBulkQuestions(@RequestBody List<Question> questions) {
        int records = questionService.insertQuestions(questions);
        return ResponseEntity.ok("Questions inserted successfully. Total records inserted: " + records + ".");
    }

    @GetMapping("/v2/questions/delete/{subject}")
    public ResponseEntity<List<Question>> deleteAllQuestionsBySubject(@PathVariable String subject) {
        List<Question> questions = questionService.deleteAllQuizQuestionsBySubject(subject);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/v2/questions/bulk/insert/subject/{subject}")
    public ResponseEntity<String> insertBulkQuestions(@PathVariable String subject, @RequestBody List<Question> questions) {
        int records = questionService.insertQuestions(subject, questions);
        return ResponseEntity.ok("Questions inserted successfully. Total records inserted: " + records + ".");
    }
}
