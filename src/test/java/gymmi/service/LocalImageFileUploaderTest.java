package gymmi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exception.class1.FileIOFailException;
import gymmi.exception.class1.InvalidFileException;
import gymmi.exception.class1.NotFoundException;
import gymmi.exception.message.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class LocalImageFileUploaderTest {

    public static final String DIRECTORY_PATH = System.getProperty("user.home") + "/temp";
    public static final String ORIGINAL_FILENAME = "파일이름.png";
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
                ORIGINAL_FILENAME,
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
                ORIGINAL_FILENAME,
                MediaType.APPLICATION_JSON_VALUE,
                new byte[]{1, 2, 3, 4, 5}
        );

        // when, then
        assertThatThrownBy(() -> uploader.upload(multipartFile, UUID.randomUUID().toString()))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage(ErrorCode.UNSUPPORTED_FILE.getMessage());
    }

    @Test
    void 이미지_파일_저장시_파일이_비어있는경우_예외가_발생한다() throws IOException {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                ORIGINAL_FILENAME,
                MediaType.APPLICATION_JSON_VALUE,
                InputStream.nullInputStream()
        );

        // when, then
        assertThatThrownBy(() -> uploader.upload(multipartFile, UUID.randomUUID().toString()))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage(ErrorCode.EMPTY_FILE.getMessage());
    }

    @Test
    void 이미지_파일_저장시_확장자가_없는경우_예외가_발생한다() throws IOException {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "파일이름",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1, 2, 3, 4, 5}
        );

        // when, then
        assertThatThrownBy(() -> uploader.upload(multipartFile, UUID.randomUUID().toString()))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage(ErrorCode.MISSING_FILE_EXTENSION.getMessage());
    }


    @Test
    void 파일_삭제를_성공한다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                ORIGINAL_FILENAME,
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
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 파일_삭제시_파일_삭제를_실패하면_예외가_발생한다() {
        // given
        LocalImageFileUploader uploader = imageFileUploader;

        // when, then
        assertThatThrownBy(() -> uploader.delete(""))
                .isInstanceOf(FileIOFailException.class);
    }

}
