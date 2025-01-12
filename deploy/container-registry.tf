resource "yandex_container_registry" "default" {
  name      = "default"
  folder_id = local.folder_id
}

resource "yandex_container_repository" "images_api_repository" {
  name = "${yandex_container_registry.default.id}/images-api"
}

output "images_api_repository_name" {
  value = "cr.yandex/${yandex_container_repository.images_api_repository.name}"
}