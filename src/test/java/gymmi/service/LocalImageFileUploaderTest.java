package gymmi.service;

import gymmi.exception.InvalidFileException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalImageFileUploaderTest {

    public static final String DIRECTORY_PATH = System.getProperty("user.home") + "/temp";
    private static String TEST_PATH = DIRECTORY_PATH + "/";
    public static final LocalImageFileUploader imageFileUploader = new LocalImageFileUploader(TEST_PATH);

    @BeforeAll
    static void 디렉터리_생성() {
        File dir = new File(DIRECTORY_PATH);
        assertThat(dir.mkdir()).isTrue();
    }

    @AfterAll
    static void 디렉터리_삭제() {
        File dir = new File(DIRECTORY_PATH);
        for (File file : dir.listFiles()) {
            assertThat(file.delete()).isTrue();
        }
        assertThat(dir.delete()).isTrue();
    }


    @Test
    void 이미지_파일_저장을_성공한다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "파일이름",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1, 2, 3, 4, 5}
        );
        LocalImageFileUploader uploader = imageFileUploader;

        // when
        String uploadedFileName = uploader.upload(multipartFile, UUID.randomUUID().toString());

        // then
        assertThat(new File(TEST_PATH + uploadedFileName).exists()).isTrue();
    }

    @Test
    void 이미지_파일_저장시_이미지_파일이_아닌경우_예외가_발생한다() {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "파일이름",
                MediaType.APPLICATION_JSON_VALUE,
                new byte[]{1, 2, 3, 4, 5}
        );

        // when, then
        assertThatThrownBy(() -> uploader.upload(multipartFile, UUID.randomUUID().toString()))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage("png 또는 jpeg 파일만 가능합니다.");
    }

    @Test
    void 이미지_파일_저장시_파일이_비어있는경우_예외가_발생한다() throws IOException {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "파일이름",
                MediaType.APPLICATION_JSON_VALUE,
                InputStream.nullInputStream()
        );

        // when, then
        assertThatThrownBy(() -> uploader.upload(multipartFile, UUID.randomUUID().toString()))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage("비어있는 파일입니다.");
    }


    @Test
    void 파일_삭제를_성공한다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "파일이름",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1, 2, 3, 4, 5}
        );
        LocalImageFileUploader uploader = imageFileUploader;
        String uploadedFileName = uploader.upload(multipartFile, UUID.randomUUID().toString());

        // then
        uploader.delete(uploadedFileName);

        // then
        assertThat(new File(TEST_PATH + uploadedFileName).exists()).isFalse();
    }

    @Test
    void 파일_삭제시_파일이_존재하지_않는_경우_예외가_발생한다() {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        // when, then
        assertThatThrownBy(() -> uploader.delete("123"))
                .hasMessage("해당 파일이 존재하지 않습니다.");
    }

    @Test
    void 파일_삭제시_파일_삭제를_실패하면_예외가_발생한다() {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        // when, then
        assertThatThrownBy(() -> uploader.delete(""))
                .hasMessage("파일 삭제에 실패하였습니다.");
    }

}
