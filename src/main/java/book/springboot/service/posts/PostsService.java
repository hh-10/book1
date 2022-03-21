package book.springboot.service.posts;

import book.springboot.domain.posts.Posts;
import book.springboot.domain.posts.PostsRepository;
import book.springboot.web.dto.PostsResponseDto;
import book.springboot.web.dto.PostsSaveRequestDto;
import book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/*
스프링에서 Bean을 주입하는 방식
@Autowired, setter, 생성자
        생성자로 Bean 객체를 받도록 하면 @Autowired와 동일한 효과를 볼 수 있다.
        final이 선언된 모든 필드를 인자값으로 하는 생성자를 롬복의 @RequiredArgsConstructor가 대신 생성해준다.
        생성자를 직접 안 쓰고 롬복 어노테이션을 사용한 이유는 해당 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 수정하는 번거로움을 해결하기 위함이다.

 */
@RequiredArgsConstructor
@Service
public class PostsService {
    private  final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        /*
        JPA의 영속성 컨텍스트: 엔티티를 영구 저장하는 환경
        트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태이다.
        이 상태에서 해당 데이터 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영한다.
        더티 체킹(dirty checking): 영속성 컨텍스트에서 Entity 객체의 값만 변경하면 별도로 update 쿼리를 날릴 필요가 없다.
         */

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        return new PostsResponseDto(entity);
    }
}
