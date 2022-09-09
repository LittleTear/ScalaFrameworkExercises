package org.littletear.zionomicon.projone.domain

import com.typesafe.config.Config

final case class FileConfig(location:String,filename:String)

object FileConfig {

  def apply(config:Config): FileConfig = FileConfig(config.getString("location"),config.getString("filename"))
}
