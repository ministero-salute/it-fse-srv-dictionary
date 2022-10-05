package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO.*;

@Document(collection = "#{@snapshotBean}")
@Data
@AllArgsConstructor
public class SnapshotETY {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_INSERTIONS = "insertions";
    public static final String FIELD_DELETIONS = "deletions";
    public static final String FIELD_INSERTION_DATE = "insertion_date";

    @Id
    private String id;

    @Field(FIELD_INSERTIONS)
    private ChunksETY insertions;

    @Field(FIELD_DELETIONS)
    private ChunksETY deletions;

    @Field(FIELD_INSERTION_DATE)
    private Date insertionDate;

    public static SnapshotETY empty() {
        return new SnapshotETY(null, ChunksETY.empty(), ChunksETY.empty(), new Date());
    }

}
