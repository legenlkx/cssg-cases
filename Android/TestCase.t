using COSXML.Common;
using COSXML.CosException;
using COSXML.Model;
using COSXML.Model.Object;
using COSXML.Model.Tag;
using COSXML.Model.Bucket;
using COSXML.Model.Service;
using COSXML.Utils;
using COSXML.Auth;
using COSXML.Transfer;
using NUnit.Framework;
using System;
using COSXML;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace COSSample
{
    public class {{name}}Sample {

      string uploadId;

      {{#steps}}
      public void {{name}}()
      {
        {{{snippet}}}
      }
      
      {{/steps}}

      [SetUp()]
      public void setup() {
        {{{setupBlock}}}
      }

      [Test()]
      public void test{{name}}() {
        {{^isDemo}}
        {{#steps}}
        {{name}}();
        {{/steps}}
        {{/isDemo}}
      }

      [TearDown()]
      public void teardown() {
        {{{teardownBlock}}}
      }
    }
}