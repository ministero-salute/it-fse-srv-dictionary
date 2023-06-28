package it.finanze.sanita.fse2.ms.edssrvdictionary.service.utils;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunkETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import lombok.Value;

@Value
public class SearchResult {
    ChunksIndexETY idx;
    ChunkETY items;
}
