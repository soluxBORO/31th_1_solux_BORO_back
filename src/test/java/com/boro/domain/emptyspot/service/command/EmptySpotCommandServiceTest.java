package com.boro.domain.emptyspot.service.command;

import com.boro.domain.emptyspot.dto.request.EmptySpotRequestDTO;
import com.boro.domain.emptyspot.repository.EmptySpotRepository;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.post.repository.PostRepository;
import com.boro.global.error.code.status.EmptySpotErrorCode;
import com.boro.global.error.exception.handler.EmptySpotException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptySpotCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EmptySpotRepository emptySpotRepository;

    @InjectMocks
    private EmptySpotCommandService emptySpotCommandService;

    // 실제 서비스 코드가 검증 시점에 LocalDateTime.now()를 다시 호출하기 때문에,
    // 테스트에서 계산한 시각과 서비스 내부 now() 사이에는 항상 약간의 실행 시간차가 생긴다.
    // 정확히 그 나노초 단위 경계를 검증하는 대신, 그 오차보다 충분히 큰 2초의 여유를 둬서
    // "경계값 포함/미포함"이 안정적으로(비결정적이지 않게) 검증되도록 한다.
    private static final long BOUNDARY_MARGIN_SECONDS = 2;

    private void mockHappyPathDependencies() {
        lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(mock(Member.class)));
        lenient().when(postRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private EmptySpotRequestDTO.CreateEmptySpot requestWithCheckoutTime(LocalDateTime expectedCheckoutTime) {
        return new EmptySpotRequestDTO.CreateEmptySpot(
                "중앙도서관 열람실", 3, 12, true, true, expectedCheckoutTime
        );
    }

    @Test
    void 퇴실예정시간이_정확히_5분_후이면_등록에_성공한다() {
        mockHappyPathDependencies();
        LocalDateTime checkoutTime = LocalDateTime.now().plusMinutes(5).plusSeconds(BOUNDARY_MARGIN_SECONDS);

        assertThatCode(() -> emptySpotCommandService.createEmptySpot(1L, requestWithCheckoutTime(checkoutTime)))
                .doesNotThrowAnyException();
    }

    @Test
    void 퇴실예정시간이_5분보다_이전이면_너무_이르다는_예외가_발생한다() {
        LocalDateTime checkoutTime = LocalDateTime.now().plusMinutes(5).minusSeconds(BOUNDARY_MARGIN_SECONDS);

        assertThatThrownBy(() -> emptySpotCommandService.createEmptySpot(1L, requestWithCheckoutTime(checkoutTime)))
                .isInstanceOf(EmptySpotException.class)
                .extracting(ex -> ((EmptySpotException) ex).getCode())
                .isEqualTo(EmptySpotErrorCode.CHECKOUT_TIME_TOO_SOON);
    }

    @Test
    void 퇴실예정시간이_정확히_20분_후이면_등록에_성공한다() {
        mockHappyPathDependencies();
        LocalDateTime checkoutTime = LocalDateTime.now().plusMinutes(20).minusSeconds(BOUNDARY_MARGIN_SECONDS);

        assertThatCode(() -> emptySpotCommandService.createEmptySpot(1L, requestWithCheckoutTime(checkoutTime)))
                .doesNotThrowAnyException();
    }

    @Test
    void 퇴실예정시간이_20분보다_이후이면_너무_늦다는_예외가_발생한다() {
        LocalDateTime checkoutTime = LocalDateTime.now().plusMinutes(20).plusSeconds(BOUNDARY_MARGIN_SECONDS);

        assertThatThrownBy(() -> emptySpotCommandService.createEmptySpot(1L, requestWithCheckoutTime(checkoutTime)))
                .isInstanceOf(EmptySpotException.class)
                .extracting(ex -> ((EmptySpotException) ex).getCode())
                .isEqualTo(EmptySpotErrorCode.INVALID_CHECKOUT_TIME);
    }

    @Test
    void 퇴실예정시간이_5분과_20분_사이의_중간값이면_등록에_성공한다() {
        mockHappyPathDependencies();
        LocalDateTime checkoutTime = LocalDateTime.now().plusMinutes(10);

        assertThatCode(() -> emptySpotCommandService.createEmptySpot(1L, requestWithCheckoutTime(checkoutTime)))
                .doesNotThrowAnyException();
    }
}
