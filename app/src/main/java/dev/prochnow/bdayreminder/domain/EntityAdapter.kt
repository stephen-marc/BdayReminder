package dev.prochnow.bdayreminder.domain

interface EntityAdapter<EntityType : Any, DataType> {
    /**
     * @return [data] decoded as type [T].
     */
    fun decode(data: DataType): EntityType

    /**
     * @return [entitu] encoded as database type [S].
     */
    fun encode(entity: EntityType): DataType

    fun EntityType.encodetoData(): DataType {
        return encode(this)
    }

    fun DataType.decodeToEntity(): EntityType {
        return decode(this)
    }
}

