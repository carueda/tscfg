package tscfg

case class ModelBuildResult(
    objectType: model.ObjectType,
    warnings: List[buildWarnings.Warning],
)
