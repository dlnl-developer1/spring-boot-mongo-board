package com.example.board;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final MongoTemplate mongoTemplate;

    public CustomPostRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Post findPostWithComments(String postId) {
        // 1. Match 특정 게시글 ID에 해당하는 문서 찾기
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(postId));

        // 2. 필요한 필드만 선택하고 _id를 String로 변환
        AggregationOperation projectToStringId = Aggregation.project("id", "title", "content")  // 필요한 필드만 선택
                .andExpression("toString(_id)").as("stringId");  // ObjectId를 문자열로 변환

        // Comments 컬렉션과 조인
        AggregationOperation lookupComments = Aggregation.lookup(
                "comments", "stringId", "postId", "comments"
        );

        // 3. Lookup으로 comments 컬렉션과 조인
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                projectToStringId,  // 변환 단계 추가
                lookupComments  // 변환 단계 추가
        );

        // 4. Aggregation 수행 및 결과 매핑
        AggregationResults<Post> results = mongoTemplate.aggregate(aggregation, "posts", Post.class);
        return results.getUniqueMappedResult();
    }
}
