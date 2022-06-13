using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SNS_Desktop.Model
{
	internal class User
	{
		public string userId;
		public string username;
		public string roleName;

		User() { }

		User(string userId, string username, string roleName)
		{
			this.userId = userId;
			this.username = username;
			this.roleName = roleName;
		}
	}
}
