locals {
  service_account_name_prefix = "images-api-sa"
}

resource "yandex_iam_service_account" "images_api_sa" {
  name        = "${local.service_account_name_prefix}-${local.folder_id}"
  description = "Service account to call images-api-container"
}

resource "yandex_iam_service_account_static_access_key" "images_api_sa_static_key" {
  service_account_id = yandex_iam_service_account.images_api_sa.id
}

output "images_api_sa_id" {
  value = yandex_iam_service_account.images_api_sa.id
}