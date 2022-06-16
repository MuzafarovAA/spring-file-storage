package ru.gb.storage.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "file_info_metadata")
public class FileMetaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "hash")
    private UUID hash;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "subtype")
    private int subtype;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getHash() {
        return hash;
    }

    public void setHash(UUID hash) {
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

}



