terraform {
  required_providers {
    yandex = {
      source = "yandex-cloud/yandex"
    }
  }
}

provider "yandex" {
  token     = "y0_AgAAAAAgHGM4AATuwQAAAAEaZIMFAADfIBJjgqFPuqBcEQiAA3T8jL1B-w"
  cloud_id  = local.cloud_id
  folder_id = local.folder_id
  zone      = local.zone
}

locals {
  cloud_id  = "b1g8rr29g0ufsbds32d0"
  folder_id = "b1gf17ep5f7rk18q841r"
  zone      = "ru-central1-b"
}