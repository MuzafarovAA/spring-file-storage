package ru.gb.storage.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gb.storage.entities.FileMetaDTO;
import ru.gb.storage.repositories.FileMetaProvider;
import ru.gb.storage.repositories.FileSystemProvider;
import ru.gb.storage.utils.HashHelper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.UUID;

@Component
public class FileStoreService {

    private FileSystemProvider systemProvider;
    private FileMetaProvider fileMetaProvider;

    @Autowired
    public void setSystemProvider(FileSystemProvider systemProvider) {
        this.systemProvider = systemProvider;
    }

    @Autowired
    public void setFileMetaProvider(FileMetaProvider fileMetaProvider) {
        this.fileMetaProvider = fileMetaProvider;
    }

    public String storeFile(byte[] content, String fileName, int subFileType) throws IOException, NoSuchAlgorithmException {
        final UUID md5 = HashHelper.getMd5Hash(content);

        String filename = fileMetaProvider.checkFileExists(md5);
        if (filename == null) {
            fileMetaProvider.saveFileMeta(md5, fileName, subFileType);
            filename = systemProvider.storeFile(content, md5, fileName);
        } else if (!filename.equals(fileName)) {
            fileMetaProvider.saveFileMeta(md5, fileName, subFileType);
        }

        return filename;
    }

    public byte[] getFile(UUID md5, String userFilename) throws IOException {
        String filename = fileMetaProvider.checkFileExistsWithFileName(md5, userFilename);
        String ext = FilenameUtils.getExtension(filename);
        String fullFileName = md5.toString() + "." + ext;
        return  systemProvider.getFile(fullFileName, userFilename);
    }

    public Collection<FileMetaDTO> getMetaFiles(int subtype) {
        return fileMetaProvider.getMetaFiles(subtype);
    }

    public void deleteFile(UUID md5, String userFilename) throws IOException {
        String filename = fileMetaProvider.checkFileExistsWithFileName(md5, userFilename);

        String ext = FilenameUtils.getExtension(filename);
        String fullFileName = md5.toString() + "." + ext;
        fileMetaProvider.deleteFileMeta(md5, userFilename);

        if (fileMetaProvider.checkFileExists(md5) == null) {
            systemProvider.deleteFile(fullFileName);
        }
    }

}