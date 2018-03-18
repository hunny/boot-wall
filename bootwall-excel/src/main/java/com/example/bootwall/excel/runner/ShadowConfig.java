package com.example.bootwall.excel.runner;

import org.apache.commons.lang3.StringUtils;

public class ShadowConfig {
  private boolean enable = true;
  private String password = null;
  private String method = null;
  private String remarks = "";
  private String server = null;
  private String obfs = "plain";
  private String protocol = "origin";
  private String group = null;
  private int server_port = 0;
  private String remarks_base64 = "";

  public ShadowConfig() {
    // Do Nothing.
  }

  public ShadowConfig(String server, //
      int server_port, //
      String password, //
      String method, //
      String group, //
      String remarks) {
    this.server = server;
    this.server_port = server_port;
    this.password = password;
    this.method = method;
    this.remarks = remarks;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getObfs() {
    return obfs;
  }

  public void setObfs(String obfs) {
    this.obfs = obfs;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getGroup() {
    if (StringUtils.isBlank(group)) {
      return "默认";
    }
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public int getServer_port() {
    return server_port;
  }

  public void setServer_port(int server_port) {
    this.server_port = server_port;
  }

  public String getRemarks_base64() {
    return remarks_base64;
  }

  public void setRemarks_base64(String remarks_base64) {
    this.remarks_base64 = remarks_base64;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (enable ? 1231 : 1237);
    result = prime * result + ((group == null) ? 0 : group.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    result = prime * result + ((obfs == null) ? 0 : obfs.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
    result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
    result = prime * result + ((remarks_base64 == null) ? 0 : remarks_base64.hashCode());
    result = prime * result + ((server == null) ? 0 : server.hashCode());
    result = prime * result + server_port;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ShadowConfig other = (ShadowConfig) obj;
    if (enable != other.enable) {
      return false;
    }
    if (group == null) {
      if (other.group != null) {
        return false;
      }
    } else if (!group.equals(other.group)) {
      return false;
    }
    if (method == null) {
      if (other.method != null) {
        return false;
      }
    } else if (!method.equals(other.method)) {
      return false;
    }
    if (obfs == null) {
      if (other.obfs != null) {
        return false;
      }
    } else if (!obfs.equals(other.obfs)) {
      return false;
    }
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (protocol == null) {
      if (other.protocol != null) {
        return false;
      }
    } else if (!protocol.equals(other.protocol)) {
      return false;
    }
    if (remarks == null) {
      if (other.remarks != null) {
        return false;
      }
    } else if (!remarks.equals(other.remarks)) {
      return false;
    }
    if (remarks_base64 == null) {
      if (other.remarks_base64 != null) {
        return false;
      }
    } else if (!remarks_base64.equals(other.remarks_base64)) {
      return false;
    }
    if (server == null) {
      if (other.server != null) {
        return false;
      }
    } else if (!server.equals(other.server)) {
      return false;
    }
    if (server_port != other.server_port) {
      return false;
    }
    return true;
  }

}
