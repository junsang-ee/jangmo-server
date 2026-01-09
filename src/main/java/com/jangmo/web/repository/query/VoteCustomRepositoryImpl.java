package com.jangmo.web.repository.query;

import com.jangmo.web.constants.vote.VoteType;
import com.jangmo.web.model.dto.request.vote.VoteListRequest;
import com.jangmo.web.model.dto.response.vote.VoteListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.jangmo.web.model.entity.vote.QVoteEntity.voteEntity;

@RequiredArgsConstructor
@Repository
public class VoteCustomRepositoryImpl implements VoteCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<VoteListResponse> findVotes(VoteListRequest request) {
		LocalDate startOfMonth = LocalDate.of(request.getYear(), request.getMonth(), 1);
		LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
		return queryFactory
			.select(Projections.constructor(
				VoteListResponse.class,
				voteEntity.id,
				voteEntity.title,
				voteEntity.voteType,
				voteEntity.startAt,
				voteEntity.endAt
			))
			.from(voteEntity)
			.where(
				voteEntity.startAt.loe(endOfMonth),
				voteEntity.endAt.goe(startOfMonth),
				voteTypeEq(request.getVoteType())
			).fetch();
    }

	private BooleanExpression voteTypeEq(VoteType voteType) {
		return voteType != null ? voteEntity.voteType.eq(voteType) : null;
	}
}
