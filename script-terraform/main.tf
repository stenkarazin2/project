data "yandex_iam_service_account" "service_account" {
  name = "service-account-1"
  folder_id = var.folder_id
}

data "yandex_iam_service_account" "node_account" {
  name = "node-account-1"
  folder_id = var.folder_id
}

#data "yandex_resourcemanager_folder_iam_binding" "editor" {
#  name = "editor-1"
#  folder_id = var.folder_id
#}

#------


resource "yandex_vpc_network" "network-1" {
  name = "network1"
}

resource "yandex_vpc_subnet" "subnet-a" {
  name           = "subneta"
  zone           = "ru-central1-a"
  network_id     = yandex_vpc_network.network-1.id
  v4_cidr_blocks = ["10.128.0.0/24"]
}

resource "yandex_vpc_subnet" "subnet-b" {
  name           = "subnetb"
  zone           = "ru-central1-b"
  network_id     = yandex_vpc_network.network-1.id
  v4_cidr_blocks = ["10.129.0.0/24"]
}

resource "yandex_vpc_subnet" "subnet-c" {
  name           = "subnetc"
  zone           = "ru-central1-c"
  network_id     = yandex_vpc_network.network-1.id
  v4_cidr_blocks = ["10.130.0.0/24"]
}



#------


resource "yandex_kubernetes_cluster" "kubernetes-1" {
  name        = "kubernetes1"
  description = "kube description1"
  network_id  = yandex_vpc_network.network-1.id # "enpffsbfdjk13q627m1n"
  folder_id   = var.folder_id

  master {
    version = "1.19"
    zonal {
      zone      = var.zone # "${yandex_vpc_subnet.subnet-b.zone}"
      subnet_id = yandex_vpc_subnet.subnet-b.id # "e9b94hi2g0emloeljvou"
    }

    public_ip = true

#    security_group_ids = ["${yandex_vpc_security_group.sg-1.id}"]
  }

  service_account_id      = data.yandex_iam_service_account.service_account.id
  node_service_account_id = data.yandex_iam_service_account.node_account.id

  labels = {
    "my_key"       = "my_value"
  }

  release_channel = "STABLE"
#  network_policy_provider = "CALICO"

  depends_on = [
    data.yandex_iam_service_account.service_account,
    data.yandex_iam_service_account.node_account #, data.yandex_resourcemanager_folder_iam_binding.editor
  ]

#  kms_provider {
#    key_id = "${yandex_kms_symmetric_key.kmssk-1.id}"
#  }

}


#------


resource "yandex_kubernetes_node_group" "node-group-1" {
  cluster_id  = yandex_kubernetes_cluster.kubernetes-1.id
  name        = "nodegroup1"
  description = "node-group description 1"
  version     = "1.19"

  labels = {
    "key" = "value"
  }
  instance_template {
    platform_id = "standard-v2"
    network_interface {
      nat        = true
      subnet_ids = [yandex_vpc_subnet.subnet-b.id]
    }
    resources {
      memory = 2
      cores  = 2
    }
    boot_disk {
      type = "network-hdd"
      size = 64
    }
    scheduling_policy {
      preemptible = false
    }
  }
  scale_policy {
    fixed_scale {
      size = 1
    }
  }
  allocation_policy {
    location {
      zone = var.zone
    }
  }

  depends_on = [
    yandex_kubernetes_cluster.kubernetes-1
  ]

}


#------


resource "yandex_mdb_mysql_cluster" "mysql-1" {
  name                = "mysql1"
  environment         = "PRODUCTION"
  network_id          = yandex_vpc_network.network-1.id
  version             = "8.0"
#  security_group_ids  = [ yandex_vpc_security_group.mysql-sg.id ]
#  deletion_protection = true

  resources {
    resource_preset_id = "s2.micro"
    disk_type_id       = "network-ssd"
    disk_size          = 20
  }

  access {
    web_sql = true
  }

  database {
    name = "ja"
  }

  user {
    name     = "jauser"
    password = "12345678"
    permission {
      database_name = "ja"
      roles         = ["ALL"]
    }
  }

  host {
    zone      = var.zone
    subnet_id = yandex_vpc_subnet.subnet-b.id
  }

#  depends_on = [
#  ]
}

